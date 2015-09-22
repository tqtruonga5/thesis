package vn.edu.hcmut.emrre.training;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorFactory;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorType;
import vn.edu.hcmut.emrre.core.utils.ReadFile;

public class EMRTrain2 {
    private static List<Relation> relations;
    private static List<Concept> concepts;
    private String model;
    private int type;
    private FeatureExtractor featureExtractor;

    public EMRTrain2(int type) {
        this.type = type;
        featureExtractor = FeatureExtractorFactory.getInstance(type);
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public void getConceptData(){
        if (EMRTrain2.concepts == null){
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/train/beth");
            EMRTrain2.concepts = read.getAllConcept(0);
            read.setFolder("i2b2data/train/partners");
            EMRTrain2.concepts.addAll(read.getAllConcept(EMRTrain2.concepts.size()));
        }
    }
    public void getRelationData(){
        if (EMRTrain2.relations == null && EMRTrain2.concepts != null){
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/train/beth");
            EMRTrain2.relations = read.getAllRelation(concepts, false);
            read.setFolder("i2b2data/train/partners");
            EMRTrain2.relations.addAll(read.getAllRelation(concepts, true));
        }
    }
    
    public static void main(String[] args) {
        EMRTrain2 train = new EMRTrain2(1);
        train.getConceptData();
        train.getRelationData();
        System.out.println(EMRTrain2.concepts.size());
        System.out.println(EMRTrain2.concepts.get(EMRTrain2.concepts.size() -1).getKey());
        System.out.println(EMRTrain2.relations.size());
    }

}
