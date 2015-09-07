package vn.edu.hcmut.emrre.core.feature;

public abstract class Feature {
    protected String name;
    protected double value;

    public Feature(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
