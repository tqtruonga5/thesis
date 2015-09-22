package vn.edu.hcmut.emrre.testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.io.DataReader;
import vn.edu.hcmut.emrre.core.svm.SVM;

public class EmrTest {
    private Double[] dataTest;
    private Relation.Type label;

    public EmrTest(Double[] dataTest) {
        this.dataTest = dataTest;
    }

    public Double[] getDataTest() {
        return dataTest;
    }

    public void set(Double[] input) {
        dataTest = input;
    }

    public Relation.Type getLabel() {
        return label;
    }

    public Relation.Type test() throws IOException {
        SVM svm = new SVM();
        double prediction = svm.svmTestCore(this.dataTest);
        this.label = Relation.typeOfDouble((int) prediction);
        return this.label;
    }

    public List<Relation> generateCandidates(List<Concept> concepts) {
        List<Relation> candidates = new ArrayList<Relation>();
        for (int i = 0; i < concepts.size() - 1; i++) {
            for (int j = i + 1; j < concepts.size(); j++) {
                Concept c1 = concepts.get(i);
                Concept c2 = concepts.get(j);
                if ((c1.getType() == Concept.Type.PROBLEM || c2.getType() == Concept.Type.PROBLEM)
                        && (c1.getLine() == c2.getLine()) && (c1.getFileName().equals(c2.getFileName()))) {
                    candidates.add(new Relation(c1.getFileName(), c1.getKey(), c2.getKey(), null, candidates.size()));
                }
            }
        }
        return candidates;
    }

    public static void main(String[] args) {
        String inputConceptFile = "i2b2data/beth/concept/record-13.con";
        DataReader dataReader = new DataReader();
        List<Concept> concepts = dataReader.readConcepts(inputConceptFile);
        for (Concept concept : concepts) {
            System.out.println(concept.toString());
        }
        
        List<Relation> relations = generateCandidates(concepts);
        for (Relation relation : relations) {
            System.out.println(relation.toString(concepts));
        }
        
    }
}
