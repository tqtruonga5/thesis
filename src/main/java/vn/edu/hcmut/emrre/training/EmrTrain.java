package vn.edu.hcmut.emrre.training;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.ContextFeatureExtractor;
import vn.edu.hcmut.emrre.core.svm.SVM;
import vn.edu.hcmut.emrre.core.utils.StanfordParserHelperImpl;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;

public class EmrTrain {
    public static final int DIMENSIONS;
    private static final String PERCENT;
    private static final String POINT;
    private static final String SHARP;
    private static final String DASH;
    public static final String FILE;
    public static List<Double[]> trainingData;
    static {
        DIMENSIONS = 15;
        PERCENT = "phantram";
        POINT = "cham";
        SHARP = "thang";
        DASH = "gachngang";
        FILE = "trainData.txt";
        trainingData = new ArrayList<Double[]>();
    }

    private boolean checkInput(Concept concept, List<CoreLabel> docline) {
        String fact = "";
        for (int i = concept.getBegin(); i < concept.getEnd(); i++) {
            fact += docline.get(i).get(TextAnnotation.class) + " ";
        }
        fact += docline.get(concept.getEnd()).get(TextAnnotation.class);
        boolean kt = concept.getContent().toLowerCase().equals(fact.toLowerCase());
        if (!kt)
            System.out.println("Concept:$" + concept.getContent().toLowerCase() + "$ ---- Docline:$"
                    + fact.toLowerCase() + "$");
        return kt;
    }

    private String encode(String docline) {
        Random ran = new Random();
        String key1 = "key1" + ran.nextInt(100);
        String key2 = "key2" + ran.nextInt(100);
        String key3 = "key3" + ran.nextInt(100);
        String key4 = "key4" + ran.nextInt(100);

        docline = docline.replaceAll(" \\.", key1);
        docline = docline.replaceAll(" %", key2);
        docline = docline.replaceAll(" # ", key3);
        docline = docline.replaceAll(" - ", key4);

        docline = docline.replaceAll("\\.", POINT);
        docline = docline.replaceAll("%", PERCENT);
        docline = docline.replaceAll("#", SHARP);
        docline = docline.replaceAll("-", DASH);
        docline = docline.replaceAll(key1, " \\.");
        docline = docline.replaceAll(key2, " %");
        docline = docline.replaceAll(key3, " # ");
        docline = docline.replaceAll(key4, " - ");
        return docline;
    }

    private String decode(String docline) {
        docline = docline.replaceAll(POINT, ".");
        docline = docline.replaceAll(PERCENT, "%");
        docline = docline.replaceAll(SHARP, "#");
        docline = docline.replaceAll(DASH, "-");

        return docline;
    }

    private List<CoreLabel> decode(List<CoreLabel> coreLabelLst) {
        for (int i = 0; i < coreLabelLst.size(); i++) {
            coreLabelLst.get(i).set(TextAnnotation.class, decode(coreLabelLst.get(i).getString(TextAnnotation.class)));
        }
        return coreLabelLst;
    }

    private void multiClassifyTraining(List<Double[]> trainingData) throws IOException {
        SVM svm = new SVM();
        svm.svmTrainCore(trainingData, trainingData.size(), DIMENSIONS);
    }

    private void generateDataTraining(List<DocLine> docLines, List<Concept> concepts, List<Relation> relations)
            throws IOException {
        int totalMiss = 0;
        long timeStart = System.nanoTime();
        for (int i = 0; i < concepts.size() - 1; i++)
            for (int j = i + 1; j < concepts.size(); j++) {
                // check if there doesn't exist relation between 2 concepts
                // add a relation NONE into relations list
                if (Relation.canRelate(concepts.get(i), concepts.get(j)))
                    if (!Relation.hasRelation(concepts.get(i), concepts.get(j))) {
                        relations.add(new Relation(concepts.get(i).getKey(), concepts.get(j).getKey(),
                                Relation.Type.NONE, relations.size()));
                    }
            }
        for (int i = 0; i < relations.size(); i++) {
            System.out.println("Relation " + i);
            Double[] aVector = new Double[DIMENSIONS + 1];
            Concept preConcept = Concept.getConcept(relations.get(i).getPreConcept(), concepts);
            Concept posConcept = Concept.getConcept(relations.get(i).getPosConcept(), concepts);
            DocLine docline = DocLine.getDocLine(docLines, preConcept.getLine());
            String encode = encode(docline.getContent());
            List<CoreLabel> coreLabelLst = StanfordParserHelperImpl.parseText2CoreLabel(encode);
            coreLabelLst = decode(coreLabelLst);
            if (!checkInput(preConcept, coreLabelLst) || !checkInput(posConcept, coreLabelLst)) {
                totalMiss++;
                continue;
            }
            try {
                aVector[0] = ContextFeatureExtractor.distance(preConcept, posConcept);
                double[] vector3dimens = ContextFeatureExtractor.preThreeWords(preConcept.getBegin(), coreLabelLst);
                aVector[1] = vector3dimens[0];
                aVector[2] = vector3dimens[1];
                aVector[3] = vector3dimens[2];
                vector3dimens = ContextFeatureExtractor.preThreeWords(posConcept.getBegin(), coreLabelLst);
                aVector[4] = vector3dimens[0];
                aVector[5] = vector3dimens[1];
                aVector[6] = vector3dimens[2];
                vector3dimens = ContextFeatureExtractor.posThreeWords(preConcept.getEnd(), coreLabelLst);

                aVector[7] = vector3dimens[0];
                aVector[8] = vector3dimens[1];
                aVector[9] = vector3dimens[2];
                vector3dimens = ContextFeatureExtractor.posThreeWords(posConcept.getEnd(), coreLabelLst);
                aVector[10] = vector3dimens[0];
                aVector[11] = vector3dimens[1];
                aVector[12] = vector3dimens[2];
                aVector[13] = ContextFeatureExtractor.lemma(preConcept, coreLabelLst);
                aVector[14] = ContextFeatureExtractor.lemma(posConcept, coreLabelLst);
                aVector[DIMENSIONS] = Relation.valueOfType(relations.get(i).getType());
                EmrTrain.trainingData.add(aVector);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error: out of bound exception:");
                System.out.println(relations.get(i).toString());
                System.out.println(docline.getContent());
                System.out.println(preConcept.toString());
                System.out.println(posConcept.toString());
                System.out.println(coreLabelLst.toString());
                totalMiss++;
                continue;
            }

        }
        System.out.println("time to training a file(7 features):" + (System.nanoTime() - timeStart));
        System.out.println("Total vector training data: " + trainingData.size());
        System.out.println("Total miss: " + totalMiss);
        System.out.println("Total relations: " + relations.size());
        // ghi file
        File file = new File(FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < trainingData.size(); i++) {
            for (int j = 0; j < DIMENSIONS; j++)
                bw.write(trainingData.get(i)[j] + "  ");
            bw.write("| " + trainingData.get(i)[DIMENSIONS]);
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }

    private Double[] parse2Double(String line) {
        Double[] result = new Double[DIMENSIONS + 1];
        String[] lst = line.split("[ ]+");
        int index = 0;
        for (int i = 0; i < lst.length; i++) {
            if (!(lst[i].trim().equals("|"))) {
                result[index++] = Double.parseDouble(lst[i].trim());
            }
        }
        return result;
    }

    public void readDataTraining(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            EmrTrain.trainingData.add(parse2Double(line));
        }
        br.close();
        fr.close();
    }

    public void training(List<DocLine> docLines, List<Concept> concepts, List<Relation> relations) throws IOException {
        // generateDataTraining(docLines, concepts, relations);
        readDataTraining(FILE);
        for (int i = 0; i < EmrTrain.trainingData.size(); i++) {
            for (int j = 0; j <= DIMENSIONS; j++)
                System.out.print(EmrTrain.trainingData.get(i)[j] + " ");
            System.out.println("\n");
        }
        // start training...
        multiClassifyTraining(trainingData);
    }
}
