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

public class SingleConceptFeatureExtractor implements FeatureExtractor{
    
    private static final int dimension = 23703;
    private double[] vector;
    private static Dictionary negativeDic;
    private static Dictionary positiveDic;
    private static Dictionary conceptDic, wicDic;
    private static Dictionary assertDic;
    private static Concept concept1, concept2;
    private SentenceDAO sentence;
    
    static {
        if (positiveDic == null){
            positiveDic = new Dictionary();
            try {
                positiveDic.getDictionaryFromFile1("file/dictionary/positive");
            } catch (IOException e) {
            }
        }
        if (negativeDic == null){
            negativeDic = new Dictionary();
            try {
                negativeDic.getDictionaryFromFile1("file/dictionary/negative");
            } catch (IOException e) {
            }
        }
        if (conceptDic == null){
            conceptDic = new Dictionary();
            try {
                conceptDic.getDictionaryFromFile1("file/dictionary/concept");
            } catch (IOException e) {
            }
        }
        if (wicDic == null){
            wicDic = new Dictionary();
            try {
                wicDic.getDictionaryFromFile1("file/dictionary/wordInConcept");
            } catch (IOException e) {
            }
        }
        if (assertDic == null){
            assertDic = new Dictionary();
            try {
                assertDic.getDictionaryFromFile1("file/dictionary/assertion");
            } catch (IOException e) {
            }
        }
    }
    
    public SingleConceptFeatureExtractor(){
        sentence = new SentenceDAOImpl();
    }
    
    private void SCF1_2(int idx){
        Sentence aSen = sentence.findByRecordAndLineIndex(concept1.getFileName(), concept1.getLine());
        if (aSen != null){
            List<Word> words = aSen.getWords();
            for (int i = concept1.getBegin(); i <= concept1.getEnd(); i++){
                    int key = wicDic.getValue(words.get(i).getLemma());
                    if (key != -1){
                        this.vector[idx + key] = 1;
                    }
            }
        }
    }
    
    private void SCF3_4(int idx){
        Sentence aSen = sentence.findByRecordAndLineIndex(concept2.getFileName(), concept2.getLine());
        if (aSen != null){
            List<Word> words = aSen.getWords();
            for (int i = concept2.getBegin(); i <= concept2.getEnd(); i++){
                    int key = wicDic.getValue(words.get(i).getLemma());
                    if (key != -1){
                        this.vector[idx + key] = 1;
                    }
            }
        }
    }
    
    private void SCF5(int idx){
        Sentence aSen = sentence.findByRecordAndLineIndex(concept1.getFileName(), concept1.getLine());
        if (aSen != null){
            List<Word> words = aSen.getWords();
            String conceptStr = "";
            for (int i = concept1.getBegin(); i <= concept1.getEnd(); i++)
                conceptStr += words.get(i).getLemma();
            int key = conceptDic.getValue(conceptStr);
            if (key != -1){
                this.vector[idx + key] = 1;
            }
        }
    }
    
    private void SCF6(int idx){
        Sentence aSen = sentence.findByRecordAndLineIndex(concept2.getFileName(), concept2.getLine());
        if (aSen != null){
            List<Word> words = aSen.getWords();
            String conceptStr = "";
            for (int i = concept2.getBegin(); i <= concept2.getEnd(); i++)
                conceptStr += words.get(i).getLemma();
            int key = conceptDic.getValue(conceptStr);
            if (key != -1){
                this.vector[idx + key] = 1;
            }
        }
    }
    
    private void SCF7_8(int idx){
        if (concept1.getType() == Concept.Type.PROBLEM || concept1.getType() == Concept.Type.PROBLEM){
            if (concept2.getType() == Concept.Type.TEST || concept2.getType() == Concept.Type.TEST)
                this.vector[idx] = 1;
            else
                if (concept1.getType() == Concept.Type.TREATMENT || concept2.getType() == Concept.Type.TREATMENT)
                    this.vector[idx+1] = 1;
                else
                    this.vector[idx+2] = 1;
        }
    }
    
    private void SCF9(int idx){
        if (concept1.getAssertion() != null){
            int key = assertDic.getValue(concept1.getAssertion());
            if (key != -1)
                this.vector[key + idx] = 1;
        }
        if (concept2.getAssertion() != null){
            int key = assertDic.getValue(concept2.getAssertion());
            if (key != -1)
                this.vector[key + idx + 6] = 1;
        }
    }
    
    private void SCF10(int idx){
        Sentence aSen = sentence.findByRecordAndLineIndex(concept1.getFileName(), concept1.getLine());
        if (aSen != null){
            List<Word> words = aSen.getWords();
            int key;
            for (int i = concept1.getBegin(); i <= concept1.getEnd(); i++)
                if ((key = positiveDic.getValue(words.get(i).getLemma())) != -1)
                    this.vector[key + idx] = 1;
            for (int i = concept2.getBegin(); i <= concept2.getEnd(); i++)
                if ((key = positiveDic.getValue(words.get(i).getLemma())) != -1)
                    this.vector[key + idx] = 1;
        }
    }

    private void SCF11(int idx){
        Sentence aSen = sentence.findByRecordAndLineIndex(concept1.getFileName(), concept1.getLine());
        if (aSen != null){
            List<Word> words = aSen.getWords();
            int key;
            for (int i = concept1.getBegin(); i <= concept1.getEnd(); i++)
                if ((key = negativeDic.getValue(words.get(i).getLemma())) != -1)
                    this.vector[key + idx] = 1;
            for (int i = concept2.getBegin(); i <= concept2.getEnd(); i++)
                if ((key = negativeDic.getValue(words.get(i).getLemma())) != -1)
                    this.vector[key + idx] = 1;
        }
    }
    
    //SCF1_2: 3556 SCF34: 3556  SCF5: 4970  SCF6: 4970 SCF7_8: 3  SCF9: 12  SCF10: 2005   SCF11: 4631
    
    public double[] buildFeatures(Relation relation) {
        if (relation.getType() != null){
            this.vector = new double[dimension + 1];
            this.vector[this.vector.length - 1] = Relation.valueOfType(relation.getType());
        }
        else{
            this.vector = new double[dimension];
        }
        concept1 = relation.getPreConcept();
        concept2 = relation.getPosConcept();
        SCF1_2(0);
        SCF3_4(3556);
        SCF5(7112);
        SCF6(12082);
        SCF7_8(17052);
        SCF9(17055);
        SCF10(17067);
        SCF11(19072);
        return vector;
    }

    public int getDimension() {
        return dimension;
    }

    
}
