package vn.edu.hcmut.emrre.core.entity.record;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.entity.word.WordDAO;
import vn.edu.hcmut.emrre.core.entity.word.WordDAOImpl;
import vn.edu.hcmut.emrre.core.io.DataReader;
import vn.edu.hcmut.emrre.core.preprocess.ProcessText;
import vn.edu.hcmut.emrre.core.preprocess.ProcessVNText;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        
    }
    
    public void createVNDatabase(){
        RecordDAO recordDAO = new RecordDAOImpl();
        SentenceDAO sentenceDAO = new SentenceDAOImpl();
        WordDAO wordDAO = new WordDAOImpl();
        ProcessText textProcessor = ProcessVNText.getInstance();

        DataReader reader = new DataReader();
         for (int j = 1; j < 688; j++) {
             System.out.println(j);
             List<DocLine> lines = reader.readDocument("vndata/txt/"+j+".txt");
             String recordContent = lines.stream().map(line -> line.getContent()).collect(Collectors.joining("\n"));

             Record record = new Record();

             record.setName(lines.get(0).getFileName().split("\\.")[0]);
             record.setText(recordContent);
             recordDAO.save(record);

             for (DocLine each : lines) {
                 Sentence sentence = new Sentence();
                 sentence.setContent(each.getContent());
                 sentence.setRecord(record);
                 sentence.setIndex(each.getLineIndex());
                 record.getSentences().add(sentence);
                 sentenceDAO.save(sentence);

                 String[] words = each.getContent().split("\\s+");
                 for (int i = 0; i < words.length; i++) {
                     Word w = new Word();
                     w.setContent(words[i]);
                     w.setIndex(i);
                     w.setPosTag(textProcessor.posTag(words[i]));
                     w.setLemma(words[i].toLowerCase());
                     sentence.getWords().add(w);
                     w.setSentence(sentence);
                     wordDAO.save(w);
                 }
             }
         }
    }
}