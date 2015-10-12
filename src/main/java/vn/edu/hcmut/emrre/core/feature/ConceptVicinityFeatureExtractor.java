package vn.edu.hcmut.emrre.core.feature;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class ConceptVicinityFeatureExtractor implements FeatureExtractor {
    private static final int dimensions = 12;
    private SentenceDAO sentence;
    private Concept first, second;
    private List<Concept> lstConcept;
    private double[] vector;
    
    public ConceptVicinityFeatureExtractor(){
        this.sentence = new SentenceDAOImpl();
    }
    
    private int valueOfConType(Concept concept){
        if (concept == null)
            return -1;
        if (concept.getType() == Concept.Type.PROBLEM)
            return 0;
        if (concept.getType() == Concept.Type.TEST)
            return 1;
        return 2;
    }
    
    private Concept find(boolean isPre){
        for (Concept concept:this.lstConcept)
            if (concept.getFileName() == first.getFileName() && concept.getLine() == first.getLine())
            if ((isPre && concept.getBegin() < first.getBegin()) || (!isPre && concept.getBegin() > second.getBegin()))
                return concept;
        return null;
    }
    
    private void CVF1(int idx){
        Concept preConcept = find(true);
        int type = valueOfConType(preConcept);
        if (type >= 0)
            this.vector[type + idx] = 1;
        this.vector[valueOfConType(first) + idx + 3] = 1;
    }
    
    private void CVF2(int idx){
        Concept preConcept = find(false);
        int type = valueOfConType(preConcept);
        if (type >= 0)
            this.vector[type + idx] = 1;
        this.vector[valueOfConType(second) + idx + 3] = 1;
    }
    
    public double[] buildFeatures(Relation relation) {
        this.first = relation.getPreConcept();
        this.second = relation.getPosConcept();
        if (relation.getType() != null){
            this.lstConcept = EMRTrain2.getConcepts();
            this.vector = new double[dimensions  + 1];
            this.vector[this.vector.length - 1] = Relation.valueOfType(relation.getType());
        }
        else{
            this.lstConcept = EmrTest.getConcepts();
            this.vector = new double[dimensions];
        }
        CVF1(0);
        CVF2(6);
        return vector;
    }

    public int getDimension() {
        return dimensions;
    }

}
