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
    private boolean sparseVector;
    private String model;
    private boolean twoRounds;
    
    public boolean isSparseVector() {
        return sparseVector;
    }

    public void setSparseVector(boolean sparseVector) {
        this.sparseVector = sparseVector;
    }

    public boolean isTwoRounds() {
        return twoRounds;
    }

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
    
    public void getAssertion(){
        if (EmrTest.concepts != null){
            ReadFile read = new ReadFile();
            read.setFolder("i2b2data/test/ast");
            read.readAllAssertion(concepts);
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
        return similarConcepts(correct.getPreConcept().getKey(), correct.getPosConcept().getKey(),
                relation.getPreConcept().getKey(), relation.getPosConcept().getKey());
    }

    private boolean correctRel(Relation correct, Relation relation) {
        return similarConcepts(correct.getPreConcept().getKey(), correct.getPosConcept().getKey(),
                relation.getPreConcept().getKey(), relation.getPosConcept().getKey())
                && correct.getType() == relation.getType();
    }

    //dataTest: [0, 1, 1, 0, 2, 0] -> result: [1, 2, 4, 1, 1, 2]
    private double[] preProcess(double[] dataTest) {
        double[] result = null;
        int size = 0;
        for (double ele : dataTest) {
            if (ele > 0)
                size++;
        }
        if (size > 0) {
            result = new double[size*2];
            int idx = 0;
            for (int i = 0; i < dataTest.length; i++) {
                if (dataTest[i] > 0) {
                    result[idx + size] = dataTest[i];
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
    
    private double[] merge(double[] vector1, double[] vector2){
        double[] result = new double[vector1.length + vector2.length];
        for (int i = 0; i < vector1.length; i++) {result[i] = vector1[i];}
        for (int i = 0; i< vector2.length; i++) {result[vector1.length + i] = vector2[i];}
        return result;
    }

    public void run() throws IOException {
        getConceptData();
        getAssertion();
        generateCandidates();
        getCorrectRelationData();
        //FeatureExtractor singleConcept = new SingleConceptFeatureExtractor();
        int[][] arr = new int[][] { {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}};
        for (Relation rel : correctRelations)
            if (rel.getType() != Relation.Type.NONE)
                arr[(int) Relation.valueOfType(rel.getType()) - 1][0]++;
        int total = 0, pRelC = 0, pNotN = 0, pNotNC = 0;
        int i = 0;
        if (this.twoRounds) {
            SVM svm1 = new SVM(model + "1");
            SVM svm2 = new SVM(model + "2");
            for (Relation relation : candidateRelations) {
                double[] dataTest = featureExtractor.buildFeatures(relation);
                if (this.sparseVector)
                    dataTest = this.preProcess(dataTest);
                if (dataTest != null) {
                    System.out.println(i++);
                    double label = svm1.svmTestCore(dataTest, this.sparseVector);
                    if (label != 0) {
                        label = svm2.svmTestCore(dataTest, this.sparseVector);
                        pNotN++;
                        relation.setType(Relation.typeOfDouble((int) label));
                        for (Relation correct : correctRelations) {
                            if (similarRel(correct, relation))
                                pNotNC++;
                            if (correctRel(correct, relation)) {
                                pRelC++;
                                arr[Relation.valueOfType(relation.getType()) - 1][1] ++;
                                System.out.println("true " + label + " " + pRelC);
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
                if (this.sparseVector)
                    dataTest = this.preProcess(dataTest);
                if (dataTest != null) {
                    System.out.println(i++);
                    double label = svm.svmTestCore(dataTest, this.sparseVector);
                    if (label != 0) {
                        pNotN++;
                        relation.setType(Relation.typeOfDouble((int) label));
                        for (Relation correct : correctRelations) {
                            if (similarRel(correct, relation))
                                pNotNC++;
                            if (correctRel(correct, relation)) {
                                arr[Relation.valueOfType(relation.getType()) - 1][1] ++;
                                pRelC++;
                                System.out.println("true " + label + " " + pRelC);
                                break;
                            }
                        }
                    }
                    total++;
                }
            }
        }
        System.out.println("\nTotal: " + correctRelations.size() + "--predict correctly: " + pRelC + "\nTotal relatived prediction: "
                + pNotN + "--predict correctly: " + pNotNC);
        float recall = (float) pRelC / correctRelations.size() * 100, precision = (float) pRelC / pNotN * 100;
        float f1 = 2 * precision * recall / (precision + recall);
        System.out.println(String.format("Precision = %f%%, Recall = %f%%, F1 = %f%%", precision, recall, f1));
        for (int j = 0; j < arr.length; j++)
            System.out.println(j + " " + arr[j][0] + " " + arr[j][1]);
        saveTestResult();
    }
}
