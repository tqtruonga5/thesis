package vn.edu.hcmut.emrre.core.feature;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import edu.stanford.nlp.ling.Word;

public class ContextFeature extends Feature {

    public ContextFeature(String name, double value) {
        super(name, value);
    }

    // the number of words between two concepts
    public static double distance(Relation relation, List<Concept> conceptLst) {
        Concept concept1 = Concept.getConcept(relation.getPreConcept(), conceptLst);
        Concept concept2 = Concept.getConcept(relation.getPosConcept(), conceptLst);
        return (concept2.getBegin() > concept1.getEnd()) ? concept2.getBegin() - concept1.getEnd() - 1 : concept1
                .getBegin() - concept2.getEnd() - 1;
    }

    double posTag(List<Word> words, Relation relation) {
        return 0;
    }

    double bigram(List<Word> words, Relation relation) {
        return 0;
    }

    // Feature[] buildFeature(){
    // return {FeatureNode(1,distance(words, relation)),
    // FeatureNode(2,0),
    // FeatureNode(3,distance(words, relation))
    // };
    // }
}
