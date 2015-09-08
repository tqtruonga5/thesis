package vn.edu.hcmut.emrre.core.entity;

public class Relation {
    private String fileName;
    private Concept preConcept;
    private Concept posConcept;

    
    public enum Type {
        TRAP, PIP
    }; 
}
