package vn.edu.hcmut.emrre.core.feature;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;

public interface FeatureExtractor {
    public List<Double> buildFeatures();
    public void setDataSource(List<Concept> concepts,List<Relation> relations);
    
}
