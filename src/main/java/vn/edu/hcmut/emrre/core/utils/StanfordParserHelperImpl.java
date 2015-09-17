package vn.edu.hcmut.emrre.core.utils;

import java.util.List;
import java.util.Properties;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.Word;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class StanfordParserHelperImpl implements StanfordParserHelper{

//    public List<Word> parseDataToListWord(String sentence, Relation relation) {
//        // TODO Auto-generated method stub
//        return null;
//    }

    public CoreMap parseDataToCoreMap(String docLine, Relation relation, List<Concept> conceptLst) {
        // TODO Auto-generated method stub
    	int aConceptBeginPos = Concept.getConcept(relation.getPreConcept(), conceptLst).getBegin();
		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		Annotation annotation = new Annotation(docLine);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence:sentences){
			int lenOfSen = (sentence.get(TokensAnnotation.class)).size();
			if (lenOfSen > aConceptBeginPos){
				return sentence;
			}
			aConceptBeginPos -= lenOfSen;		
		}
		return null;
    }

    public CoreMap getSentenceContainRel(String docLine, Relation relation) {
        // TODO Auto-generated method stub
    	
		return null;
    }

}
