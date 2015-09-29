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
    private static final int dimension = 5325;
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

    private void bagOfWord(String fileName, int lineIndex, int startConcept, int endConcept, int index){
        int size = this.dictionary.getSize();
        for (int i = index; i < index + size; i++)
            this.vector[i] = 0;
        WriteFile wf = new WriteFile("lactInDB");
        try {
            wf.open(true);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try{
        List<Word> wordLst = this.sentence.findByRecordAndLineIndex(fileName, lineIndex).getWords();
        int start = (startConcept >= 3) ? startConcept - 3: 0;
        int end = (endConcept + 4 <= wordLst.size()) ? endConcept + 3 : wordLst.size() - 1;  
        for(int i = start; i <= end; i++){
            int key = this.dictionary.getValue(wordLst.get(i).getLemma());
            if (key != -1 && key < index + size){
                this.vector[key] = 1;
            }
        }
        }
        catch (Exception e){
            try {
                wf.writeln("File name: " + fileName + ", line: " + lineIndex);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        try {
            wf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public double[] buildFeatures(Relation relation) {
        if (relation.getType() != null){
            this.vector = new double[dimension + 1];
            this.vector[dimension] = Relation.valueOfType(relation.getType());
        }
        else{
            this.vector = new double[dimension];
        }
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
        if (preConcept.getBegin() < postConcept.getBegin())
            bagOfWord(preConcept.getFileName(), preConcept.getLine(), preConcept.getBegin(), postConcept.getEnd(), 0);
        else
            bagOfWord(preConcept.getFileName(), preConcept.getLine(), postConcept.getBegin(), preConcept.getEnd(), 0);
        
        return this.vector;
    }

    public int getDimension() {
        return dimension;
    }


}
