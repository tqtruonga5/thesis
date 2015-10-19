package vn.edu.hcmut.emrre.core.feature;

import vn.edu.hcmut.emrre.core.feature.wiki.WikipediaFeatureExtractor;

public class FeatureExtractorFactory {

    public static FeatureExtractor getInstance(int type) {
        switch (type) {
        case FeatureExtractorType.CONTEXT:
            return new ContextFeatureExtractor();

        case FeatureExtractorType.SIMILARITY:
            return new SimilarityFeaturesExtractor();

        case FeatureExtractorType.SINGLE_CONCEPT:
            return new SingleConceptFeatureExtractor();

        case FeatureExtractorType.WIKIPEDIA:
            return new WikipediaFeatureExtractor();

        case FeatureExtractorType.CONCEPT_VICINITY:
            return new ConceptVicinityFeatureExtractor();
            
        case FeatureExtractorType.COMBINE:
            return new Combine();
            
        default:
            return null;
        }
    }
}
