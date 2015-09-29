package vn.edu.hcmut.emrre.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorFactory;
import vn.edu.hcmut.emrre.core.svm.SVM;
import vn.edu.hcmut.emrre.core.utils.ReadFile;
import vn.edu.hcmut.emrre.core.utils.WriteFile;

public class EMRTrain2 {
    private static List<Relation> relations;
    private static List<Concept> concepts;
    private boolean twoRounds;
    private String model;
    private String trainDataFile;
    private int type;
    private FeatureExtractor featureExtractor;

    public EMRTrain2(int type) {
        this.twoRounds = false;
        this.type = type;
        featureExtractor = FeatureExtractorFactory.getInstance(type);
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
    
    public void run() throws IOException, InvalidInputDataException {
        getConceptData();
        getRelationData();
        WriteFile wf = new WriteFile(this.trainDataFile);
        wf.open(false);
        for (Relation relation : relations) {
            double[] vector = featureExtractor.buildFeatures(relation);
            int count = 0;
            for (int j = 0; j < vector.length - 1; j++){
                if (vector[j] == 0)
                    count ++;
            }
            if (vector.length - count > 1){
                wf.write("" + (int)vector[vector.length - 1]);
                for (int j = 0; j < vector.length - 1; j++){
                    if (vector[j] != 0)
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

}
