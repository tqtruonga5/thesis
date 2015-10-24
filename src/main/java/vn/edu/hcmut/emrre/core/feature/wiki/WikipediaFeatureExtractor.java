package vn.edu.hcmut.emrre.core.feature.wiki;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.testing.EmrTest;

public class WikipediaFeatureExtractor implements FeatureExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaFeatureExtractor.class);
    private double[] vector;
    private int dimension = 5;
    private WikiDataWrapper preConceptData;
    private WikiDataWrapper posConceptData;

    public double[] buildFeatures(Relation relation) {
        int preConceptKey = relation.getPreConcept().getKey();
        int posConceptKey = relation.getPosConcept().getKey();

        WikiDataSource dataSource = WikiDataSource.getInstance();

        if (relation.getType() != null) {
            this.vector = new double[dimension + 1];
            this.vector[dimension] = relation.getType().getValue();
            dataSource.loadDataTrain();
        } else {
            this.vector = new double[dimension];
            dataSource.loadDataTest();
        }

        this.preConceptData = dataSource.getData().get(preConceptKey);
        this.posConceptData = dataSource.getData().get(posConceptKey);

        this.vector[0] = wf1();
        this.vector[1] = wf2();
        this.vector[2] = wf3();
        this.vector[3] = wf4();
        this.vector[4] = wf5();

        return this.vector;
    }

    private int wf1() {
        if (preConceptData.getTitles().size() != 0 || posConceptData.getTitles().size() != 0) {
            return 1;
        }
        return 0;
    }

    private int wf2() {
        for (String title : preConceptData.getTitles()) {
            if (posConceptData.getTitles().contains(title)) {
                return 1;
            }
        }
        return 0;
    }

    private int wf3() {
        for (String title : preConceptData.getTitles()) {
            if (posConceptData.getLinks().contains(title)) {
                return 1;
            }
        }
        return 0;
    }

    private int wf4() {
        for (String title : posConceptData.getTitles()) {
            if (preConceptData.getLinks().contains(title)) {
                return 1;
            }
        }
        return 0;
    }

    private int wf5() {
        for (String link : posConceptData.getLinks()) {
            if (preConceptData.getLinks().contains(link)) {
                return 1;
            }
        }
        return 0;
    }

    public int getDimension() {
        return this.dimension;
    }

    public static void main(String[] args) {
        // EMRTrain2 emrTrain2 = new EMRTrain2(0);
        // emrTrain2.getConceptData();
        // emrTrain2.getRelationData();
        //
        // List<Relation> relations = emrTrain2.getRelations();
        EmrTest test = new EmrTest(0);
        test.getConceptData();
        test.generateCandidates();
        List<Relation> relations = test.candidateRelations;

        WikipediaFeatureExtractor extractor = new WikipediaFeatureExtractor();
        for (Relation relation : relations.subList(0, 200)) {
            try {
                double[] result = extractor.buildFeatures(relation);
                for (double d : result) {
                    System.out.print(d + "\t");
                }
                System.out.println();
            } catch (Exception e) {
                LOGGER.error(relation.getPreConcept().getFileName() + " - " + relation.getPosConcept().getFileName(), e);
            }
        }

    }
}
