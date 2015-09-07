package vn.edu.hcmut.emrre.core.feature;

import java.util.ArrayList;
import java.util.List;

public class FeatureVector {
    private List<Feature> features;
    private int size;
    private double classValue;

    public FeatureVector(double classValue, int size) {
        this.classValue = classValue;
        this.size = size;
        features = new ArrayList<Feature>(size);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public int getSize() {
        return size;
    }

    public double getClassValue() {
        return classValue;
    }

    public void setClassValue(double classValue) {
        this.classValue = classValue;
    }
    
    public static void main(String[] args) {
        FeatureVector featureVector = new FeatureVector(1, 5);
        featureVector.getFeatures().get(2);
    }

}
