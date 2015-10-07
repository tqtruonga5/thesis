package vn.edu.hcmut.emrre.core.feature.similarity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;

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
        return 0;
    }

    public double[] buildFeatures(Relation relation) {
        if (relation.getType() != null) {
            this.vector = new double[dimension + 1];
            this.vector[dimension] = relation.getType().getValue();
        } else {
            this.vector = new double[dimension];
        }

        SimilaritySequence sequence = new SimilaritySequence();
        sequence.setPosTagSequences(DataPreprocess.posSequence(relation));
        sequence.setLemmaSequences(DataPreprocess.lemmaSequence(relation));
        sequence.setPhraseChunks(DataPreprocess.phraseChunkSequence(relation));
        sequence.setShortestPaths(DataPreprocess.shortestPath(relation));

        SimilarityDataHandler dataHandler = SimilarityDataHandler.getInstance();
        double[] posResult = dataHandler.statisticPosDistance(sequence);
        double[] lemmaResult = dataHandler.statisticLemmaDistance(sequence);
        double[] phraseChunkResult = dataHandler.statisticPhraseChunksDistance(sequence);
        double[] shortestPathResult = dataHandler.statisticShortestPathDistance(sequence);

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

    public static void main(String[] args) {
        // EMRTrain2 emr = new EMRTrain2(0);
        // emr.getConceptData();
        // emr.getRelationData();
        // SimilarityFeaturesExtractor extractor = new
        // SimilarityFeaturesExtractor();
        // extractor.buildFeatures(EMRTrain2.getRelations().get(4));

    }
}
