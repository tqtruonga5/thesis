package vn.edu.hcmut.emrre.core.entity;

public class Concept {
    private String fileName;
    private String lexicon;
    private TYPE type;
    private int begin;
    private int end;
    private int line;

    public Concept(String lexicon, int begin, int end, TYPE type) {
        this.lexicon = lexicon;
        this.begin = begin;
        this.end = end;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("c=%s - begin=%d - end=%d - type=%s", lexicon, begin, end, type.name());
    }

    public static enum TYPE {
        PROBLEM, TREATMENT, TEST
    }
    
    public static void main(String[] args) {
        Concept concept = new Concept("panadol", 1, 5, Concept.TYPE.TREATMENT);
        System.out.println(concept.toString());
    }
}
