package vn.edu.hcmut.emrre.core.utils;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import edu.stanford.nlp.util.CoreMap;

public interface StanfordParserHelper {
    public List<Word> parseDataToListWord(String sentence,Relation relation);
    public CoreMap parseDataToCoreMap(String docLine,Relation relation, List<Concept> conceptLst);
    public CoreMap getSentenceContainRel(String docLine, Relation relation);
}
