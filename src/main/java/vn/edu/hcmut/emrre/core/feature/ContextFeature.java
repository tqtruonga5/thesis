package vn.edu.hcmut.emrre.core.feature;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.Word;

public class ContextFeature extends Feature {
    
    public ContextFeature(String name, double value) {
        super(name, value);
    }

    double distance(List<Word> words,Relation relation){
        return 0;
    }
    
    double posTag(List<Word> words,Relation relation){
        return 0;
    }
    
    double bigram(List<Word> words,Relation relation){
        return 0;
    }
    
//    Feature[] buildFeature(){
//        return {FeatureNode(1,distance(words, relation)),
//            FeatureNode(2,0),
//            FeatureNode(3,distance(words, relation))
//                };
//    }
}
