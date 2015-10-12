package vn.edu.hcmut.emrre.core.feature.similarity;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class NLPHelper {
    public static NLPHelper nlpHelper;

    public static NLPHelper getInstance() {
        if (nlpHelper == null) {
            nlpHelper = new NLPHelper();
        }
        return nlpHelper;
    }

    private String text;
    StanfordCoreNLP pipeline;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NLPHelper() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse");
        pipeline = new StanfordCoreNLP(props);

    }

    public Annotation getAnnotatedDoc() {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        return document;
    }

    public static void main(String[] args) {
        NLPHelper nlpHelper = NLPHelper.getInstance();
        nlpHelper.setText(" PROBLEM reveal TEST.");

        Annotation doc = nlpHelper.getAnnotatedDoc();
        CoreMap Sen = doc.get(SentencesAnnotation.class).get(0);
        Tree tree = (Tree) Sen.get(TreeAnnotation.class);
        System.err.println(tree);
        List<CoreLabel> tokens = doc.get(TokensAnnotation.class);
        for (CoreLabel coreLabel : tokens) {
            System.out.println(coreLabel.get(PartOfSpeechAnnotation.class));
        }

    }
}
