package vn.edu.hcmut.emrre.testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private boolean twoRounds;
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

    public void setTwoRounds(boolean isTwoRounds) {
        this.twoRounds = isTwoRounds;
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
            EmrTest.candidateRelations = new ArrayList<Relation>();
            for (int i = 0; i < concepts.size() - 1; i++)
                for (int j = i + 1; j < concepts.size(); j++) {
                    if (Relation.canRelate(concepts.get(i), concepts.get(j))) {
                        EmrTest.candidateRelations.add(new Relation(concepts.get(i).getFileName(), concepts.get(i),
                                concepts.get(j), null, EmrTest.candidateRelations.size()));
                    }
                }
        }
    }

    // check
    private boolean similarConcepts(int a1, int b1, int a2, int b2) {
        return (a1 == a2 && b1 == b2) || (a1 == b2 && b1 == a2);
    }

    private boolean similarRel(Relation correct, Relation relation) {
        return similarConcepts(correct.getPreConcept().getKey(), correct.getPosConcept().getKey(), relation
                .getPreConcept().getKey(), relation.getPosConcept().getKey());
    }

    private boolean correctRel(Relation correct, Relation relation) {
        return similarConcepts(correct.getPreConcept().getKey(), correct.getPosConcept().getKey(), relation
                .getPreConcept().getKey(), relation.getPosConcept().getKey())
                && correct.getType() == relation.getType();
    }

    private double[] preProcess(double[] dataTest) {
        double[] result = null;
        int size = 0;
        for (double ele : dataTest) {
            if (ele > 0)
                size++;
        }
        if (size > 0) {
            result = new double[size];
            int idx = 0;
            for (int i = 0; i < dataTest.length; i++) {
                if (dataTest[i] > 0) {
                    result[idx++] = i + 1;
                }
            }
        }
        return result;
    }

    public void saveTestResult() throws IOException {
        // luu theo dinh dang I2B2
        File file = new File("result.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        Concept pre, post;
        for (Relation relation : candidateRelations) {
            pre = relation.getPreConcept();
            post = relation.getPosConcept();
            bw.write(String.format("c=\"%s\" %d:%d %d:%d||r=\"%s\"||c=\"%s\" %d:%d %d:%d", pre.getContent(),
                    pre.getLine(), pre.getBegin(), pre.getLine(), pre.getEnd(), relation.getType(), post.getContent(),
                    post.getLine(), post.getBegin(), post.getLine(), post.getEnd()));
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }

    public void run() throws IOException {

        getConceptData();
        generateCandidates();
        getCorrectRelationData();
        int[] arr = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        for (Relation rel : correctRelations)
            if (rel.getType() != Relation.Type.NONE)
                arr[(int) Relation.valueOfType(rel.getType()) - 1]++;
        int total = 0, pRelC = 0, pNotN = 0, pNotNC = 0;
        int i = 0;
        if (this.twoRounds) {
            SVM svm1 = new SVM(model + "1");
            SVM svm2 = new SVM(model + "2");
            for (Relation relation : candidateRelations) {
                System.out.println(i++);
                double[] dataTest = featureExtractor.buildFeatures(relation);
                // dataTest = this.preProcess(dataTest);
                if (dataTest != null) {
                    // double label = svm1.svmTestCore(dataTest, true);
                    double label = svm1.svmTestCore(dataTest);
                    if (label != 0) {
                        // label = svm2.svmTestCore(dataTest, true);
                        label = svm2.svmTestCore(dataTest);
                        pNotN++;
                        relation.setType(Relation.typeOfDouble((int) label));
                        for (Relation correct : correctRelations) {
                            if (similarRel(correct, relation))
                                pNotNC++;
                            if (correctRel(correct, relation)) {
                                System.out.println("true " + label);
                                pRelC++;
                                break;
                            }
                        }
                    }
                    total++;
                }
            }
        } else {
            SVM svm = new SVM(model);
            for (Relation relation : candidateRelations) {
                double[] dataTest = featureExtractor.buildFeatures(relation);
                // dataTest = this.preProcess(dataTest);
                // if(i == 10 ) return;

                if (dataTest != null) {
                    System.out.println(i++);
                    double label = svm.svmTestCore(dataTest);
                    if (label != 0) {
                        pNotN++;
                        relation.setType(Relation.typeOfDouble((int) label));
                        for (Relation correct : correctRelations) {
                            if (similarRel(correct, relation))
                                pNotNC++;
                            if (correctRel(correct, relation)) {
                                System.out.println("true " + label);
                                pRelC++;
                                break;
                            }
                        }
                    }
                    total++;
                }
            }
        }
        for (int j = 0; j < arr.length; j++)
            System.out.print(arr[j] + " ");
        System.out.println("\nTotal: " + total + "--predict correctly: " + pRelC + "\nTotal relatived prediction: "
                + pNotN + "--predict correctly: " + pNotNC);
        saveTestResult();

    }
}
