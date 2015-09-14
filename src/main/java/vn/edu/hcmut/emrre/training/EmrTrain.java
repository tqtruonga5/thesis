package vn.edu.hcmut.emrre.training;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.io.DataReader;

public class EmrTrain {
    private int numOfTraining;

    public void training(List<DocLine> docLines, List<Concept> concepts, List<Relation> relations) {
        // duyet tat ca cac file
        // 1 file

        // chua tat ca data training
        List<Double[][]> trainingData;
        System.out.println(relations.size());
        long timeStart = System.nanoTime();
        for (int i = 0; i < concepts.size() - 1; i++)
            for (int j = i + 1; j < concepts.size(); j++) {
                // check if there doesn't exist relation between 2 concepts
                // add a relation NONE into relations list
                if (Relation.canRelate(concepts.get(i), concepts.get(j)))
                    if (!Relation.hasRelation(concepts.get(i), concepts.get(j),
                            DocLine.getDocLine(docLines, concepts.get(i).getLine()))) {
                        relations.add(new Relation(concepts.get(i).getKey(), concepts.get(j).getKey(),
                                Relation.Type.NONE, relations.size()));
                    }
            }
        long time = System.nanoTime() - timeStart;
        System.out.println(relations.size());
        System.out.println("Time run: " + time);
    }
}
