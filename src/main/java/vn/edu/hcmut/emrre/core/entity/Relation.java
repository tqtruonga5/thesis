package vn.edu.hcmut.emrre.core.entity;

public class Relation {
    private String fileName;
    private Concept preConcept;
    private Concept posConcept;
    private Type type;

    public Relation(String fileName, Concept preConcept, Concept posConcept, Type type) {
        this.preConcept = preConcept;
        this.posConcept = posConcept;
        this.type = type;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Concept getPreConcept() {
        return preConcept;
    }

    public void setPreConcept(Concept preConcept) {
        this.preConcept = preConcept;
    }

    public Concept getPosConcept() {
        return posConcept;
    }

    public void setPosConcept(Concept posConcept) {
        this.posConcept = posConcept;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        TrIP, TrAP, TrNAP, TrCP, TrWP, TeRP, TeCP, PIP
    }
    
    public static double valueOfType(Type type){
    	switch (type){
    	case TrIP:
    		return 1;
    	case TrAP:
    		return 2;
    	case TrNAP: 
    		return 3;
    	case TrCP: 
    		return 4;
    	case TrWP:
    		return 5;
    	case TeRP: 
    		return 6;
    	case TeCP: 
    		return 7;
    	default: 
    		return 8;
    	}
    }

    @Override
    public String toString() {
        return String.format("%s | %s |%s", preConcept.toString(),type,posConcept.toString());
    };
    
    

    // public static void main(String[] args) {
    // System.out.println(Type.valueOf("TrIP"));
    // }
}
