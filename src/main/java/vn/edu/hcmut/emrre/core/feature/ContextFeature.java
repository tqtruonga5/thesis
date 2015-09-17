package vn.edu.hcmut.emrre.core.feature;

import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.Word;

public class ContextFeature extends Feature {
    private static HashMap<String, Double> dictionary;

    private static double autoValue;

    public ContextFeature(String name, double value) {
        super(name, value);
    }

    static {
        autoValue = 0;
        dictionary = new HashMap<String, Double>();
    }

    // the number of words between two concepts
    public static double distance(Concept preConcept, Concept posConcept) {
        return (posConcept.getBegin() > preConcept.getEnd()) ? posConcept.getBegin() - preConcept.getEnd() - 1
                : preConcept.getBegin() - posConcept.getEnd() - 1;
    }

    public static double[] preThreeWords(int conceptPosition, List<CoreLabel> tokens) {
        double[] result = new double[3];
        // List<CoreLabel> tokens = coreMap.get(TokensAnnotation.class);
        for (int i = 3; i >= 1; i--) {
            if (conceptPosition - i >= 0) {

                String word = tokens.get(conceptPosition - i).get(LemmaAnnotation.class).toLowerCase();

                if (ContextFeature.dictionary.get(word) == null) {
                    ContextFeature.dictionary.put(word, ContextFeature.autoValue++);
                }
                result[3 - i] = ContextFeature.dictionary.get(word);
            } else {
                result[3 - i] = -1;
            }
        }
        return result;
    }

    public static double[] posThreeWords(int conceptPosition, List<CoreLabel> tokens) {
        double[] result = new double[3];
        // List<CoreLabel> tokens = coreMap.get(TokensAnnotation.class);
        for (int i = 1; i <= 3; i++) {
            if (conceptPosition + i < tokens.size()) {
                String word = tokens.get(conceptPosition + i).get(LemmaAnnotation.class).toLowerCase();
                if (ContextFeature.dictionary.get(word) == null) {
                    ContextFeature.dictionary.put(word, ContextFeature.autoValue++);
                }
                result[i - 1] = ContextFeature.dictionary.get(word);
            } else {
                result[i - 1] = -1;
            }
        }
        return result;
    }

    public static double lemma(Concept concept, List<CoreLabel> tokens) {
        // List<CoreLabel> tokens = coreMap.get(TokensAnnotation.class);
        String words = "";
        for (int i = concept.getBegin(); i <= concept.getEnd(); i++) {
            words += tokens.get(i).getString(LemmaAnnotation.class).toLowerCase();
        }
        if (ContextFeature.dictionary.get(words) == null) {
            ContextFeature.dictionary.put(words, ContextFeature.autoValue++);
        }
        return ContextFeature.dictionary.get(words);
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
