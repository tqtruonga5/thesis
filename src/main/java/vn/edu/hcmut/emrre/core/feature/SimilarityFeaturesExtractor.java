package vn.edu.hcmut.emrre.core.feature;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;

public class SimilarityFeaturesExtractor implements FeatureExtractor {

    private List<Concept> concepts;
    private List<Relation> relations;

    public void setDataSource(List<Concept> concepts, List<Relation> relations) {
        this.concepts = concepts;
        this.relations = relations;
    }
    
    

    public List<Double> buildFeatures() {
        return null;
    }
}
