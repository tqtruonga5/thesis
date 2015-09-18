package vn.edu.hcmut.emrre.core.utils;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.Word;

public class StanfordParserHelperImpl {
    protected static StanfordCoreNLP pipeline;
    protected static List<CoreMap> coreMapCache;
    static {
        pipeline = new StanfordCoreNLP();
        coreMapCache = new ArrayList<CoreMap>();
    }

    public List<CoreMap> parseDataFromDocLines(List<DocLine> doclines) {
        System.out.println("Number of sentences: ");
        List<CoreMap> result = new ArrayList<CoreMap>();
        for (DocLine line : doclines) {
            Annotation annotation = new Annotation(line.getContent());
            StanfordParserHelperImpl.pipeline.annotate(annotation);
            System.out.println("Line " + line.getLineIndex() + "  " + annotation.get(CoreAnnotations.SentencesAnnotation.class).size());
            result.add(line.getLineIndex() - 1, annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0));
            
        }
        return result;
    }

    public List<Word> parseDataToListWord(String sentence, Relation relation) {
        return null;
    }

    public static CoreMap parseDataToCoreMap(Concept concept, String docLine) {
        int aConceptBeginPos = concept.getBegin();
        Annotation annotation = new Annotation(docLine);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            int lenOfSen = (sentence.get(TokensAnnotation.class)).size();
            if (lenOfSen > aConceptBeginPos) {
                return sentence;
            }
            aConceptBeginPos -= lenOfSen;
        }
        return null;
    }
    
    public static List<CoreLabel> parseText2CoreLabel(String docLine) {
        List<CoreLabel> result = new ArrayList<CoreLabel>();
        Annotation annotation = new Annotation(docLine);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token:sentence.get(TokensAnnotation.class)){
                result.add(token);
            }
        }
        return result;
    }

    public CoreMap getSentenceContainRel(String docLine, Relation relation) {
        // TODO Auto-generated method stub

        return null;
    }

}
