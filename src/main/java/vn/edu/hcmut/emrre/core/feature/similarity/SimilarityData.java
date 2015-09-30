package vn.edu.hcmut.emrre.core.feature.similarity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.io.DataReader;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class SimilarityData {
    public static List<SimilaritySequence> sequences = new ArrayList<SimilaritySequence>();
    public static final int MAX_MOST_POS = 100;

    public static void buildData(List<Relation> relations) {
        SimilarityFeaturesExtractor sf = new SimilarityFeaturesExtractor();

        for (Relation relation : relations) {
            SimilaritySequence data = new SimilaritySequence();
            data.setPosTagSequences(sf.posSequence(relation));
            data.setLemmaSequences(sf.lemmaSequence(relation));
//            data.setShortestPaths(sf.shortestPath(relation));
            data.setRelType(relation.getType());
            SimilarityData.sequences.add(data);
        }
    }

    public static void statistic() {
        for (SimilaritySequence each1 : sequences) {
            Queue<DataObject> q = new PriorityQueue<DataObject>();
            for (SimilaritySequence each2 : sequences) {
                if (each1.equals(each2)) {
                    continue;
                }

                int distance = LevenshteinDistance.computeDistance(each1.getPosTagSequences(),
                        each2.getPosTagSequences());

                if (q.size() < MAX_MOST_POS) {
                    q.add(new DataObject(distance, (int) Relation.valueOfType(each2.getRelType())));
                } else {
                    if (q.peek().getDistance() > distance) {
                        q.poll();
                        q.add(new DataObject(distance, (int) Relation.valueOfType(each2.getRelType())));
                    }
                }
            }
            DataStatistic dataStatistic = new DataStatistic(q);
            dataStatistic.compute();
            System.out.println(dataStatistic.toString() + "\t type:" + Relation.valueOfType(each1.getRelType()));
        }

    }

    private static class DataStatistic {

        @Override
        public String toString() {
            return String.format("0:%f|1:%f|2:%f|3:%f|4:%f|5:%f|6:%f|7:%f|8:%f", percentage[0], percentage[1],
                    percentage[2], percentage[3], percentage[4], percentage[5], percentage[6], percentage[7],
                    percentage[8]);
        }

        private Queue<DataObject> dataObjects;
        private double[] percentage = new double[9];

        public double[] getPercentage() {
            return percentage;
        }

        public void setPercentage(double[] percentage) {
            this.percentage = percentage;
        }

        public DataStatistic() {
        }

        public DataStatistic(Queue<DataObject> objects) {
            this.dataObjects = objects;
        }

        public Queue<DataObject> getDataObjects() {
            return dataObjects;
        }

        public void setDataObjects(Queue<DataObject> dataObjects) {
            this.dataObjects = dataObjects;
        }

        public void compute() {
            for (DataObject each : dataObjects) {
                percentage[each.getType()] += 1;
            }

            for (int i = 0; i < percentage.length; i++) {
                percentage[i] = percentage[i] / dataObjects.size();
            }
        }
    }

    private static class DataObject implements Comparable<DataObject> {
        private int distance;
        private int type;

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public DataObject() {
        }

        public DataObject(int distance, int type) {
            this.distance = distance;
            this.type = type;
        }

        @Override
        public int compareTo(DataObject o) {
            return o.distance - this.distance;
        }
    }

    // Comparator anonymous class implementation
    public static Comparator<Integer> idComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer c1, Integer c2) {
            return (int) (c2 - c1);
        }
    };

    public static void main(String[] args) {

//        String inputConceptFile = "i2b2data/train/beth/concept/record-13.con";
//        String inputRelationFile = "i2b2data/train/beth/rel/record-13.rel";
//        DataReader dataReader = new DataReader();
//        List<Concept> concepts = dataReader.readConcepts(inputConceptFile, 0);
        EMRTrain2 emr = new EMRTrain2(0);
        emr.getConceptData();
        emr.getRelationData();
        
//        List<Relation> relations = emr.relations;

        SimilarityData data = new SimilarityData();
        data.buildData(EMRTrain2.getRelations());

        data.statistic();
    }
}
