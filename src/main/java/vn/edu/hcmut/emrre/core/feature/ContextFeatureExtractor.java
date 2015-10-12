package vn.edu.hcmut.emrre.core.feature;

import java.io.IOException;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.utils.Dictionary;

public class ContextFeatureExtractor implements FeatureExtractor {
    private static final int dimension = 21668;
    private double[] vector;
    private static Dictionary bagDic, bigramDic, threeDic;
    private static Dictionary[] typeDics;
    private SentenceDAO sentence;
    private static String[] posTag;

    static{
        if (posTag == null)
            posTag = new String[]{ "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN", "NNS", "NNP",
                    "NNPS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD", "VBG", "VBN",
                    "VBP", "VBZ", "WDT", "WP", "WP$", "WRB" };
        if (bagDic == null){
            bagDic = new Dictionary();
            try {
                bagDic.getDictionaryFromFile1("file/dictionary/bag");
            } catch (IOException e) {
            }
        }
        if (bigramDic == null){
            bigramDic = new Dictionary();
            try {
                bigramDic.getDictionaryFromFile1("file/dictionary/bigram");
            } catch (IOException e) {
            }
        }
        if (threeDic == null){
            threeDic = new Dictionary();
            try {
                threeDic.getDictionaryFromFile1("file/dictionary/three");
            } catch (IOException e) {
            }
        }
        typeDics = new Dictionary[8];
        for (int i = 0; i< 8; i++){
            typeDics[i] = new Dictionary();
            try {
                typeDics[i].getDictionaryFromFile("file/dictionary/types/" + Relation.typeOfDouble(i+1));
            } catch (IOException e) {
            }
        }
    }
    public ContextFeatureExtractor() {
        sentence = new SentenceDAOImpl();
    }
    
    private double[] normalize(double[] vector, int begin){
        double[] offset = {13.32, 1.19, 19.10, 5.99, 20.45, 1, 6.31, 1.44};
        double t = 0;
        for (int i = 0; i < 8; i++){
            vector[begin + i] *= offset[i];
            t += vector[begin + i];
        }
        if (t != 0)
            for (int i = 0; i < 8; i++)
                vector[begin + i] /= t;
        return vector;
    }

    private void posBetweenRel(Concept preConcept, Concept postConcept, int index) {
        Sentence aSentence = this.sentence.findByRecordAndLineIndex(preConcept.getFileName(), preConcept.getLine());
        if (aSentence != null) {
            List<Word> wordLst = aSentence.getWords();
            for (int i = preConcept.getEnd() + 1; i < postConcept.getBegin(); i++)
                for (int j = 0; j < this.posTag.length; j++)
                    if (this.posTag[j].equals(wordLst.get(i).getPosTag())) {
                        this.vector[j + index] = 1;
                        break;
                    }
        }
    }
    
    private void bigram(Concept preConcept, Concept postConcept, int idx){
        Sentence aSentence = this.sentence.findByRecordAndLineIndex(preConcept.getFileName(), preConcept.getLine());
        if (aSentence != null){
            List<Word> wordLst = aSentence.getWords();
            for (int i = preConcept.getEnd() + 1; i < postConcept.getBegin() - 1; i++){
                int key = bigramDic.getValue(wordLst.get(i).getLemma() + wordLst.get(i+1).getLemma());
                if (key != -1) {
                    this.vector[key + idx] = 1;
                }
            }
        }
    }
    
    private void threeWord(Concept preConcept, Concept postConcept, int idx) {
        Sentence aSentence = this.sentence.findByRecordAndLineIndex(preConcept.getFileName(), preConcept.getLine());
        if (aSentence != null){
            List<Word> wordLst = aSentence.getWords();
                if (preConcept.getBegin() >= 3) {
                    int idx1 = preConcept.getBegin();
                    int key = threeDic.getValue(wordLst.get(idx1 - 3).getLemma() + wordLst.get(idx1 - 2).getLemma()
                            + wordLst.get(idx1 - 1).getLemma());
                    if (key != -1) {
                        this.vector[key + idx] = 1;
                    }
                }
                if (postConcept.getBegin() >= 3) {
                    int idx1 = postConcept.getBegin();
                    int key = threeDic.getValue(wordLst.get(idx1 - 3).getLemma() + wordLst.get(idx1 - 2).getLemma()
                            + wordLst.get(idx1 - 1).getLemma());
                    if (key != -1) {
                        this.vector[key + idx] = 1;
                    }
                }
        }
    }

    private void bagOfWord(Concept preConcept, Concept postConcept, int idx){
        Sentence aSentence = this.sentence.findByRecordAndLineIndex(preConcept.getFileName(), preConcept.getLine());
        if (aSentence != null){
            List<Word> wordLst = aSentence.getWords();
            for (int i = preConcept.getBegin() - 3; i <= postConcept.getEnd() + 3; i++)
                if (i >= 0 && i < wordLst.size())
                    for (int j = 0; j < 8; j++){
                        int key = typeDics[j].getValue(wordLst.get(i).getLemma());
                        if (key > 0){
                            this.vector[j + idx] += 1;
                        }
                    }
            //this.vector = normalize(this.vector, idx);
        }
    }
    
    private void bagOfWord1(Concept preConcept, Concept postConcept, int idx){
        Sentence aSentence = this.sentence.findByRecordAndLineIndex(preConcept.getFileName(), preConcept.getLine());
        if (aSentence != null){
            List<Word> wordLst = aSentence.getWords();
            for (int i = preConcept.getBegin() - 3; i <= postConcept.getEnd() + 3; i++)
                if (i >= 0 && i < wordLst.size())
                    for (int j = 0; j < 8; j++){
                        int key = bagDic.getValue(wordLst.get(i).getLemma());
                        if (key != -1){
                            this.vector[idx + key] = 1;
                        }
                    }
        }
    }

    private void predicate(String recordName, int sentenceIdx, int idx) {
        Sentence aSentence = this.sentence.findByRecordAndLineIndex(recordName, sentenceIdx);
        if (aSentence != null) {
            if (aSentence.getPredicate() != null) {
                int key = bagDic.getValue(aSentence.getPredicate());
                if (key != -1 && key + idx < dimension)
                    this.vector[key + idx] = 1;
            }
        }
    }

    private void typeOfConcepts(Concept preConcept, Concept postConcept, int idx) {
        if (preConcept.getType() == Concept.Type.PROBLEM || postConcept.getType() == Concept.Type.PROBLEM) {
            if (preConcept.getType() == Concept.Type.TEST || postConcept.getType() == Concept.Type.TEST)
                this.vector[idx] = 1;
            else if (preConcept.getType() == Concept.Type.TREATMENT || postConcept.getType() == Concept.Type.TREATMENT)
                this.vector[idx + 1] = 1;
            else
                this.vector[idx + 2] = 1;
        }
    }
    
    //bagOfWord: 5068, predicate: 8, posBetweenRel: 36, threeWord: 4930, bigram: 11631, typeOfConcepts: 3
    public double[] buildFeatures(Relation relation) {
        if (relation.getType() != null) {
            this.vector = new double[dimension + 1];
            this.vector[dimension] = Relation.valueOfType(relation.getType());
        } else {
            this.vector = new double[dimension];
            this.vector[this.vector.length - 1] = 0;
        }
        for (int i = 0; i < this.vector.length - 1; i++)
            this.vector[i] = 0;
        Concept preConcept, postConcept;
        preConcept = relation.getPreConcept();
        postConcept = relation.getPosConcept();

        bagOfWord1(preConcept, postConcept, 0);
        //predicate(preConcept.getFileName(), preConcept.getLine(), 5068);
        posBetweenRel(preConcept, postConcept, 5068);
        bigram(preConcept, postConcept, 5104);
        threeWord(preConcept, postConcept, 16735);
        typeOfConcepts(preConcept, postConcept, 21665);
        return this.vector;
    }

    public int getDimension() {
        return dimension;
    }

}
