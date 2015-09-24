package vn.edu.hcmut.emrre.core.entity.record;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.entity.word.WordDAO;
import vn.edu.hcmut.emrre.core.entity.word.WordDAOImpl;
import vn.edu.hcmut.emrre.core.utils.StanfordParserHelperImpl;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        WordDAO wordDAO = new WordDAOImpl();
        List<Word> words = wordDAO.findByIdCondition(514L);

        for (Word word : words) {
            try {
                String lemma = StanfordParserHelperImpl.parseText2CoreLabel(word.getContent()).get(0).lemma();
                word.setLemma(lemma);
                wordDAO.update(word);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

        }

    }
}