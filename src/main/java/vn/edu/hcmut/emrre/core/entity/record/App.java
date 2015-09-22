package vn.edu.hcmut.emrre.core.entity.record;

import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;

public class App {
    public static void main(String[] args) {
        SentenceDAO sentenceDAO = new SentenceDAOImpl();
        Sentence sentence = sentenceDAO.findByRecordAndLineIndex("record-13", 12);
        System.err.println(sentence);
        System.out.println(sentence.getWords());

    }
}