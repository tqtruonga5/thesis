package vn.edu.hcmut.emrre.testing;

import java.io.IOException;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorFactory;
import vn.edu.hcmut.emrre.core.svm.SVM;
import vn.edu.hcmut.emrre.core.utils.ReadFile;

public class EmrTest {
    private static List<Relation> correctRelations;
    private static List<Concept> concepts;
    private static List<Relation> candidateRelations;
    private String model;
    private int type;
    private FeatureExtractor featureExtractor;

    public EmrTest(int type) {
        this.type = type;
        featureExtractor = FeatureExtractorFactory.getInstance(type);
    }

    public FeatureExtractor getFeatureExtractor() {
        return featureExtractor;
    }

    public void setFeatureExtractor(FeatureExtractor featureExtractor) {
        this.featureExtractor = featureExtractor;
    }

    public static List<Relation> getRelations() {
        return correctRelations;
    }

    public static void setRelations(List<Relation> relations) {
        EmrTest.correctRelations = relations;
    }

    public static List<Concept> getConcepts() {
        return concepts;
    }

    public static void setConcepts(List<Concept> concepts) {
        EmrTest.concepts = concepts;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void getConceptData() {
        if (EmrTest.concepts == null) {
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/test/concepts");
            EmrTest.concepts = read.getAllConcept(0);
        }
    }

    public void getCorrectRelationData() {
        if (EmrTest.correctRelations == null && EmrTest.concepts != null) {
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/test/rel");
            EmrTest.correctRelations = read.getAllRelation(concepts, false);
        }
    }

    public void generateCandidates() {
        if (EmrTest.candidateRelations == null) {
            for (int i = 0; i < concepts.size() - 1; i++)
                for (int j = i + 1; j < concepts.size(); j++) {
                    if (Relation.canRelate(concepts.get(i), concepts.get(j))) {
                        EmrTest.candidateRelations.add(new Relation(concepts.get(i).getFileName(), concepts.get(i),
                                concepts.get(j), null, EmrTest.candidateRelations.size()));

                    }
                }
        }
    }

    public void run() throws IOException {
        getConceptData();
        generateCandidates();
        getCorrectRelationData();
        SVM svm = new SVM(model, featureExtractor.getDimension(), null);
        for (Relation relation : candidateRelations) {
            double[] dataTest = featureExtractor.buildFeatures(relation);
            double label = svm.svmTestCore(dataTest);
            relation.setType(Relation.typeOfDouble((int) label));
        }
    }

    public void saveTestResult() {
        // luu theo dinh dang I2B2
    }

}
