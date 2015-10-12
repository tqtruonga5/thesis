package vn.edu.hcmut.emrre.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorFactory;
import vn.edu.hcmut.emrre.core.feature.SingleConceptFeatureExtractor;
import vn.edu.hcmut.emrre.core.svm.SVM;
import vn.edu.hcmut.emrre.core.utils.Dictionary;
import vn.edu.hcmut.emrre.core.utils.ReadFile;
import vn.edu.hcmut.emrre.core.utils.WriteFile;

public class EMRTrain2 {
    private static List<Relation> relations;
    private static List<Concept> concepts;
    private boolean twoRounds;
    private boolean parentRelation;
    private boolean sparseVector;
    private String trainDataFile;
    private int type;
    private String model;
    private FeatureExtractor featureExtractor;

    public EMRTrain2(int type) {
        this.twoRounds = false;
        this.type = type;
        featureExtractor = FeatureExtractorFactory.getInstance(type);
    }

    public boolean isSparseVector() {
        return sparseVector;
    }

    public void setSparseVector(boolean sparseVector) {
        this.sparseVector = sparseVector;
    }

    public boolean isParentRelation() {
        return parentRelation;
    }

    public void setParentRelation(boolean parentRelation) {
        this.parentRelation = parentRelation;
    }

    public void setTwoRounds(boolean isTwoRound){
        this.twoRounds = isTwoRound;
    }
    
    public FeatureExtractor getFeatureExtractor() {
        return featureExtractor;
    }

    public void setFeatureExtractor(FeatureExtractor featureExtractor) {
        this.featureExtractor = featureExtractor;
    }

    public static List<Relation> getRelations() {
        return relations;
    }

    public static void setRelations(List<Relation> relations) {
        EMRTrain2.relations = relations;
    }

    public static List<Concept> getConcepts() {
        return concepts;
    }

    public static void setConcepts(List<Concept> concepts) {
        EMRTrain2.concepts = concepts;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    
    public String getTrainDataFile() {
        return trainDataFile;
    }

    public void setTrainDataFile(String fileName) {
        this.trainDataFile = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void getConceptData() {
        if (EMRTrain2.concepts == null) {
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/train/beth/concept");
            EMRTrain2.concepts = read.getAllConcept(0);
            read.setFolder("i2b2data/train/partners/concept");
            EMRTrain2.concepts.addAll(read.getAllConcept(EMRTrain2.concepts.size()));
        }
    }

    public void getRelationData() {
        if (EMRTrain2.relations == null && EMRTrain2.concepts != null) {
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/train/beth/rel");
            EMRTrain2.relations = read.getAllRelation(concepts, false);
            read.setFolder("i2b2data/train/partners/rel");
            EMRTrain2.relations.addAll(read.getAllRelation(concepts, true));
        }
    }
    
    public void getAssertion(){
        if (concepts != null){
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/train/beth/ast");
            read.readAllAssertion(concepts);
            read.setFolder("i2b2data/train/partners/ast");
            read.readAllAssertion(concepts);
        }
    }
    
    private void splitFile(String fName1, String fName2) throws IOException{
        File fo = new File(this.trainDataFile);
        FileReader fr = new FileReader(fo);
        BufferedReader br = new BufferedReader(fr);
        WriteFile f1 = new WriteFile(fName1);
        WriteFile f2 = new WriteFile(fName2);
        f1.open(false); f2.open(false);
        String line;
        while((line = br.readLine()) != null){
            int key = Integer.parseInt(line.substring(0, 1));
            if (key > 0){
                f1.writeln("1" + line.substring(1));
                f2.writeln(line);
            }else{
                f1.writeln(line);
            }
        }
        f1.close(); f2.close();
        br.close(); fr.close();
        
    }
    
    private double[] merge(double[] vector1, double[] vector2){
        double[] result = new double[vector1.length + vector2.length - 1];
        for (int i = 0; i < vector1.length - 1; i++) result[i] = vector1[i];
        for (int i = 0; i< vector2.length; i++) result[vector1.length + i - 1] = vector2[i];
        return result;
    }
    
    public void run() throws IOException, InvalidInputDataException {
        getConceptData();
        getAssertion();
        getRelationData();
//        List<Relation> temp = new ArrayList<Relation>();
//        int[] arr = new int[]{19,2, 16, 5, 45,1,6, 1};
//        for (Relation relation: relations){
//            if (Relation.valueOfType(relation.getType()) != 0)
//                for (int j = 0; j <= arr[Relation.valueOfType(relation.getType()) - 1]; j++)
//                    temp.add(relation);
//        }
//        temp.addAll(relations);
//        relations = temp;
        WriteFile wf = new WriteFile(this.trainDataFile);
        wf.open(false);
        for (Relation relation : relations) {
            double[] vector = featureExtractor.buildFeatures(relation);
            wf.write("" + (int)vector[vector.length - 1]);
            int count = 0;
            if (this.sparseVector){
                for (int j = 0; j < vector.length - 1; j++){
                    if (vector[j] == 0)
                        count ++;
                }
                if (vector.length - count > 1){
                    for (int j = 0; j < vector.length - 1; j++){
                        if (vector[j] != 0)
                            wf.write(" " + (j + 1) + ":" + (vector[j]));
                    }
                    wf.writeln("");
                }
            }
            else{
                for (int j = 0; j < vector.length - 1; j++){
                        wf.write(" " + (j + 1) + ":" + (vector[j]));
                }
                wf.writeln("");
            }
        }
        wf.close();
        if (this.twoRounds){
            String file1 = this.trainDataFile + "1";
            String file2 = this.trainDataFile + "2";
            splitFile(file1, file2);
            SVM svm1 = new SVM(model + "1");
            svm1.svmTrainCore(new File(file1));
            SVM svm2 = new SVM(model + "2");
            svm2.svmTrainCore(new File(file2));
        }else{
            SVM svm = new SVM(model);
            svm.svmTrainCore(new File(this.trainDataFile));
        }
            System.out.println("Train process successfully");
    }

    public static void main(String[] args) throws IOException, InvalidInputDataException {    
        //construct dictionary
        EMRTrain2 emr = new EMRTrain2(7);
        emr.getConceptData();
        emr.getRelationData();
        emr.getAssertion();
//        Dictionary[] dic = new Dictionary[8];
//        for (int i = 0; i < 8; i++)
//            dic[i] = new Dictionary();
//        Dictionary wordInConcept = new Dictionary();
//        Dictionary concept = new Dictionary();
        Dictionary assertion = new Dictionary();
        SentenceDAO sentence = new SentenceDAOImpl();
        int ir = 0;
        for (Concept concept: concepts){
            try{
                if (concept.getAssertion() != null)
                    assertion.addDictionary(concept.getAssertion());
//                int type = Relation.valueOfType(relation.getType());
//                if (type > 0){
//                    System.out.println(ir++);
//                    List<Word> wordLst = sentence.findByRecordAndLineIndex(relation.getFileName(), relation.getPreConcept().getLine()).getWords();
//                    Concept first = relation.getPreConcept(), second = relation.getPosConcept();
//                    String sconcept = "";
//                    for (int i = first.getBegin(); i <= first.getEnd(); i++){
//                        wordInConcept.addDictionary(wordLst.get(i).getLemma());
//                        sconcept += wordLst.get(i).getLemma();
//                    }
//                    concept.addDictionary(sconcept);
//                    sconcept = "";
//                    for (int i = second.getBegin(); i <= second.getEnd(); i++){
//                        wordInConcept.addDictionary(wordLst.get(i).getLemma());
//                        sconcept += wordLst.get(i).getLemma();
//                    }
//                    concept.addDictionary(sconcept);
//                    dic[type-1].addDictionary1(sconcept);
//                    sconcept = "";
//                    for (int i = second.getBegin(); i <= second.getEnd(); i++)
//                        sconcept += wordLst.get(i).getLemma();
//                    dic[type-1].addDictionary1(sconcept);
//                    for (int i = first.getBegin() - 3; i < second.getEnd() + 3; i++)
//                        if (i >= 0 && i < wordLst.size()) dic[type-1].addDictionary1(wordLst.get(i).getLemma());
//                    if (first.getBegin() >= 3) dic[type-1].addDictionary1(wordLst.get(first.getBegin() - 3).getLemma() + wordLst.get(first.getBegin() - 2).getLemma() + wordLst.get(first.getBegin() - 1).getLemma());
//                    if (second.getBegin() >= 3) dic[type-1].addDictionary1(wordLst.get(second.getBegin() - 3).getLemma() + wordLst.get(second.getBegin() - 2).getLemma() + wordLst.get(second.getBegin() - 1).getLemma());
//                    for (int i = first.getEnd() + 1; i < second.getBegin() - 1; i++) dic[type-1].addDictionary1(wordLst.get(i).getLemma() + wordLst.get(i + 1).getLemma());
//                }
            }catch (Exception e){}
        }
//        concept.saveDictionary2File("file/dictionary/concept");
//        wordInConcept.saveDictionary2File("file/dictionary/wordInConcept");
    //    dictionary1.saveDictionary2File("file/dictionary/bag");
    //    dictionary2.saveDictionary2File("file/dictionary/bigram");
    //    dictionary3.saveDictionary2File("file/dictionary/threeWord");
        //conceptDic.saveDictionary2File("file/dictionary/concept");
        assertion.saveDictionary2File("file/dictionary/assertion");
//        for (int i = 1; i <= 8; i++)
//            dic[i - 1].saveDictionary2File("file/dictionary/types/" + Relation.typeOfDouble(i));

    }
}
