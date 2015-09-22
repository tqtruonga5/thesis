package vn.edu.hcmut.emrre.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Concept {

    private String fileName;
    private String content;
    private Type type;
    private int begin;
    private int end;
    private int line;
    private int key;
    private List<Integer> relateLst;

    public Concept(String fileName, String content, int line, int begin, int end, Type type, int key) {
        this.fileName = fileName;
        this.content = content;
        this.line = line;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.key = key;
        this.relateLst = new ArrayList<Integer>();
    }

    public List<Integer> getRelateLst() {
        return this.relateLst;
    }

    public void addRelateLst(int key) {
        relateLst.add(key);
    }

    public static Concept getConcept(int key, List<Concept> conceptLst) {
        return conceptLst.get(key);
    }

    public int getKey() {
        return this.key;
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
        return String.format(
                "file name=%s - c=%s -lineIndex =%d - begin=%d - end=%d - type=%s - key=%d - number of relate followed=%d", fileName, content,
                line, begin, end, type, key, this.relateLst.size());
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
