import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vn.edu.hcmut.emrre.core.entity.record.Record;
import vn.edu.hcmut.emrre.core.entity.record.RecordDAO;
import vn.edu.hcmut.emrre.core.entity.record.RecordDAOImpl;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.entity.word.WordDAO;
import vn.edu.hcmut.emrre.core.entity.word.WordDAOImpl;

public class TestVNDatabase {
    public SentenceDAO sentenceDAO;
    public WordDAO wordDAO;
    public RecordDAO recordDAO;

    @Before
    public void startUp() {
        sentenceDAO = new SentenceDAOImpl();
        wordDAO = new WordDAOImpl();
        recordDAO = new RecordDAOImpl();
    }

    @Test
    public void testGetVNData() {
        Record r = recordDAO.findByName("686");
        List<Sentence> sentences = r.getSentences();
        for (Sentence sentence : sentences) {
            System.out.println(sentence.getWords());
        }
    }
}
