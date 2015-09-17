package vn.edu.hcmut.emrre.core.entity;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Relation {
    private String fileName;
    private int preConcept;
    private int posConcept;
    private Type type;
    private int key;

    public Relation(String fileName, int preConcept, int posConcept, Type type, int key) {
        this.preConcept = preConcept;
        this.posConcept = posConcept;
        this.type = type;
        this.fileName = fileName;
        this.key = key;
    }

    public Relation(int preConcept, int posConcept, Type type, int key) {
        this.preConcept = preConcept;
        this.posConcept = posConcept;
        this.type = type;
        this.key = key;
    }

    public static Relation getRelation(int key, List<Relation> relateLst) {
        return relateLst.get(key);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getPreConcept() {
        return preConcept;
    }

    public void setPreConcept(int preConcept) {
        this.preConcept = preConcept;
    }

    public int getPosConcept() {
        return posConcept;
    }

    public void setPosConcept(int posConcept) {
        this.posConcept = posConcept;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getKey() {
        return key;
    }

    public static enum Type {
        TrIP, TrAP, TrNAP, TrCP, TrWP, TeRP, TeCP, PIP, NONE
    }

    public static boolean hasRelation(Concept first, Concept second, DocLine docLine) {
        // TODO Auto-generated method stub
        boolean check = false;
        // if (docLine != null & first.getLine() == second.getLine() &
        // inASentences(first.getBegin(), second.getBegin(),
        // docLine.getContent())){

        if (first.getLine() == second.getLine()) {
            List<Integer> relateLst1 = first.getRelateLst();
            for (int i = 0; i < relateLst1.size(); i++) {
                if (relateLst1.get(i) == second.getKey()) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public static boolean canRelate(Concept first, Concept second) {
        return first.getType() == Concept.Type.PROBLEM || second.getType() == Concept.Type.PROBLEM;
    }

    public static boolean inASentences(int begin1, int begin2, String line) {
        boolean check = false;
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP stan = new StanfordCoreNLP(props);
        Annotation annotation = new Annotation(line);
        stan.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        int currentPos = 0;
        for (CoreMap sentence : sentences) {
            currentPos += (sentence.get(TokensAnnotation.class)).size();
            boolean check1 = currentPos > begin1;
            boolean check2 = currentPos > begin2;
            if (check1 || check2) {
                check = !(check1 ^ check2);
                break;
            }
        }
        return check;
    }

    public static double valueOfType(Type type) {
        switch (type) {
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
        case NONE:
            return 8;
        default:
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%s | %s |%s", preConcept, type, posConcept);
    };

    // public static void main(String[] args) {
    // System.out.println(Type.valueOf("TrIP"));
    // }
}
