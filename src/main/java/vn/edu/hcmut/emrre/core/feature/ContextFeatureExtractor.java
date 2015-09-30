package vn.edu.hcmut.emrre.core.feature;

import java.io.IOException;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.utils.Dictionary;
import vn.edu.hcmut.emrre.core.utils.WriteFile;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class ContextFeatureExtractor implements FeatureExtractor{
    private static final int dimension = 21855;
    private double[] vector;
    private Dictionary dictionary;
    private SentenceDAO sentence;
    
    public ContextFeatureExtractor() {
        dictionary = new Dictionary();
        try{
            dictionary.getDictionaryFromFile();
        }catch(IOException e){
        }
        sentence = new SentenceDAOImpl();
    }
    
    public Dictionary getDictionary(){
        return this.dictionary;
    }
    
    public void setDictionary(Dictionary dictionary){
        this.dictionary = dictionary;
    }
    // the number of words between two concepts
    private void distance(Concept preConcept, Concept posConcept, int index) {
        this.vector[index] = (posConcept.getBegin() > preConcept.getEnd()) ? posConcept.getBegin() - preConcept.getEnd() - 1
                : preConcept.getBegin() - posConcept.getEnd() - 1;
    }

    private void preThreeWords(Concept concept, int index) {
        String fileName = concept.getFileName();
        int lineIndex = concept.getLine();
        int conceptPosition = concept.getBegin();
        List<Word> wordLst = sentence.findByRecordAndLineIndex(fileName, lineIndex).getWords();
        for (int i = 3; i >= 1; i--) {
            if (conceptPosition - i >= 0) {
                String word = wordLst.get(conceptPosition - i).getLemma();
                dictionary.addDictionay(word);
                this.vector[index++] = dictionary.getValue(word);
            } else {
                this.vector[index++] = -1;
            }
        }
    }

    public void posThreeWords(Concept concept, int index) {
        String fileName = concept.getFileName();
        int lineIndex = concept.getLine();
        int conceptPosition = concept.getBegin();
        List<Word> wordLst = this.sentence.findByRecordAndLineIndex(fileName, lineIndex).getWords();
        for (int i = 1; i <= 3; i++) {
            if (conceptPosition + i < wordLst.size()) {
                String word = wordLst.get(conceptPosition + i).getLemma();
                dictionary.addDictionay(word);
                this.vector[index++] = dictionary.getValue(word);
            } else {
                this.vector[index++] = -1;
            }
        }
    }

    public void lemma(Concept concept, int index) {
        String fileName = concept.getFileName();
        int lineIndex = concept.getLine();
        int conceptBegin = concept.getBegin();
        int conceptEnd = concept.getEnd();
        List<Word> wordLst = this.sentence.findByRecordAndLineIndex(fileName, lineIndex).getWords();
        String word = "";
        for (int i = conceptBegin; i <= conceptEnd; i++){
            if (i < wordLst.size())
                word += wordLst.get(i).getLemma();
        }
        dictionary.addDictionay(word);
        this.vector[index] = dictionary.getValue(word);

    }

    private void bagOfWord(Concept preConcept, Concept postConcept, int index){
        int size = this.dictionary.getSize();
        WriteFile wf = new WriteFile("lactInDB");
        try {
            wf.open(true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try{
            List<Word> wordLst = this.sentence.findByRecordAndLineIndex(preConcept.getFileName(), preConcept.getLine()).getWords();
            int start = (preConcept.getBegin() >= 3) ? preConcept.getBegin() - 3: 0;
            int end = (postConcept.getEnd() + 4 <= wordLst.size()) ? postConcept.getEnd() + 3 : wordLst.size() - 1;  
            for(int i = start; i <= end; i++){
                int key = this.dictionary.getValue(wordLst.get(i).getLemma());
                if (key != -1 && key < index + size){
                    this.vector[key] = 1;
                }
            }
            //Any bigram between two concepts
            for (int i = preConcept.getEnd() + 1; i < postConcept.getBegin() - 1; i++){
                int key = this.dictionary.getValue(wordLst.get(i).getLemma() + wordLst.get(i).getLemma());
                if (key != -1 && key < index + size){
                    this.vector[key] = 1;
                }
            }
            //three words succeeding concept
            if (preConcept.getBegin() >= 3){
                int idx = preConcept.getBegin();
                int key = this.dictionary.getValue(wordLst.get(idx - 3).getLemma() + wordLst.get(idx - 2).getLemma() + wordLst.get(idx - 1).getLemma());
                if (key != -1 && key < index + size){
                    this.vector[key] = 1;
                }
            }
            if (postConcept.getBegin() >= 3){
                int idx = postConcept.getBegin();
                int key = this.dictionary.getValue(wordLst.get(idx - 3).getLemma() + wordLst.get(idx - 2).getLemma() + wordLst.get(idx - 1).getLemma());
                if (key != -1 && key < index + size){
                    this.vector[key] = 1;
                }
            }
        }
        catch (Exception e){
            try {
                wf.writeln("File name: " + preConcept.getFileName() + ", line: " + preConcept.getLine());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            wf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private void typeOfConcepts(Concept preConcept, Concept postConcept, int idx){
        if (preConcept.getType() == Concept.Type.PROBLEM || postConcept.getType() == Concept.Type.PROBLEM){
            if (preConcept.getType() == Concept.Type.TEST || postConcept.getType() == Concept.Type.TEST)
                this.vector[idx] = 1;
            else
                if (preConcept.getType() == Concept.Type.TREATMENT || postConcept.getType() == Concept.Type.TREATMENT)
                    this.vector[idx + 1] = 1;
                else
                    this.vector[idx + 2] = 1;
        }
    }
    
    public double[] buildFeatures(Relation relation) {
        if (relation.getType() != null){
            this.vector = new double[dimension + 1];
            this.vector[dimension] = Relation.valueOfType(relation.getType());
        }
        else{
            this.vector = new double[dimension];
            this.vector[this.vector.length - 1] = 0;
        }
        for (int i = 0; i < this.vector.length - 1; i++) this.vector[i] = 0;
        Concept preConcept, postConcept;
        preConcept = relation.getPreConcept();
        postConcept = relation.getPosConcept();
//        this.distance(preConcept, postConcept, 0);
//        this.preThreeWords(preConcept, 1);
//        this.preThreeWords(postConcept, 4);
//        this.posThreeWords(preConcept, 7);
//        this.posThreeWords(postConcept, 10);
//        this.lemma(preConcept, 13);
//        this.lemma(postConcept, 14);
//        this.vector[15] = Relation.valueOfType(relation.getType());
        bagOfWord(preConcept, postConcept, 0);
        typeOfConcepts(preConcept, postConcept, 21852);
        return this.vector;
    }

    public int getDimension() {
        return dimension;
    }


}
