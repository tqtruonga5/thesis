package vn.edu.hcmut.emrre.core.entity.record;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SentenceDAO sentenceDAO = new SentenceDAOImpl();
        List<Sentence> sentences = sentenceDAO.findAll();
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        int i = 0;
        for (Sentence sentence : sentences) {
            if (sentence.getPredicate() == null) {
                try {
                    System.out.println(i++);
                    String text = sentence.getContent();
                    Annotation annotation = new Annotation(text);
                    pipeline.annotate(annotation);
                    for (CoreMap core : annotation.get(SentencesAnnotation.class)) {
                        SemanticGraph graph = core.get(CollapsedCCProcessedDependenciesAnnotation.class);

                        sentence.setPredicate(graph.getFirstRoot().lemma());

                    }
                    sentenceDAO.save(sentence);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // System.out.println(sentence.getWords());

    }
}