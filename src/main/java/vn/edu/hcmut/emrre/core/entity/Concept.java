package vn.edu.hcmut.emrre.core.entity;

public class Concept {
    
    
    private String fileName;
    private String content;
    private Type type;
    private int begin;
    private int end;
    private int line;

    public Concept(String fileName, String content, int line, int begin, int end, Type type) {
        this.fileName = fileName;
        this.content = content;
        this.line = line;
        this.begin = begin;
        this.end = end;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
    

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("c=%s -lineIndex =%d - begin=%d - end=%d - type=%s", content,line, begin, end, type);
    }

    public static enum Type {
        PROBLEM, TREATMENT, TEST
    }

    public static void main(String[] args) {
        // Concept concept = new Concept("panadol", 1, 5,
        // Concept.TYPE.TREATMENT);
        // System.out.println(concept.toString());
    }
}
