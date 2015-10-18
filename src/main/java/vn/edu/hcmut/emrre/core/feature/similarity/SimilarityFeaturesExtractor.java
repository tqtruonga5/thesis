package vn.edu.hcmut.emrre.core.feature.similarity;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class SimilarityFeaturesExtractor implements FeatureExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(SimilarityFeaturesExtractor.class);
    private double[] vector;
    private int dimension = 45;
    List<Concept> concepts;

    public SimilarityFeaturesExtractor() {
    }

    public SimilarityFeaturesExtractor(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public int getDimension() {
        return this.dimension;
    }

    public double[] buildFeatures(Relation relation) {
        List<Concept> concepts  = Collections.emptyList();
        if (relation.getType() != null) {
            this.vector = new double[dimension + 1];
            this.vector[dimension] = relation.getType().getValue();
            concepts = EMRTrain2.getConcepts();
        } else {
            this.vector = new double[dimension];
            concepts = EmrTest.getConcepts();
        }

        SimilaritySequence sequence = new SimilaritySequence();
        sequence.setPosTagSequences(DataPreprocess.posSequence(relation));
        sequence.setLemmaSequences(DataPreprocess.lemmaSequence(relation));
        sequence.setPhraseChunks(DataPreprocess.phraseChunkSequence(relation));
        sequence.setShortestPaths(DataPreprocess.shortestPath(relation));
        sequence.setAllConceptType(DataPreprocess.conceptTypeSequence(relation, concepts));

        SimilarityDataHandler dataHandler = SimilarityDataHandler.getInstance();
        double[] posResult = dataHandler.statisticPosDistance(sequence);
        double[] lemmaResult = dataHandler.statisticLemmaDistance(sequence);
        double[] phraseChunkResult = dataHandler.statisticPhraseChunksDistance(sequence);
        double[] shortestPathResult = dataHandler.statisticShortestPathDistance(sequence);
        double[] conceptTypeResult = dataHandler.statisticConceptTypeDistance(sequence);

        for (int i = 0; i < posResult.length; i++) {
            this.vector[i] = posResult[i];
        }
        for (int i = 0; i < lemmaResult.length; i++) {
            this.vector[i + 9] = lemmaResult[i];
        }
        for (int i = 0; i < phraseChunkResult.length; i++) {
            this.vector[i+18] = phraseChunkResult[i];
        }
        for (int i = 0; i < shortestPathResult.length; i++) {
            this.vector[i + 27] = shortestPathResult[i];
        }
        for (int i = 0; i < conceptTypeResult.length; i++) {
            this.vector[i + 36] = conceptTypeResult[i];
        }
        
        return this.vector;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }

    public int getDemension() {
        return dimension;
    }

    public void setDemension(int dimension) {
        this.dimension = dimension;
    }
}
