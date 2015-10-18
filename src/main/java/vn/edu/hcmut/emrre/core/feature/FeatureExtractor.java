package vn.edu.hcmut.emrre.core.feature;

import vn.edu.hcmut.emrre.core.entity.Relation;


public interface FeatureExtractor {    
    public double[] buildFeatures(Relation relation);
    public int getDimension();
}
