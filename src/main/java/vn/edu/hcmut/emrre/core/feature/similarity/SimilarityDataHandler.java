package vn.edu.hcmut.emrre.core.feature.similarity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class SimilarityDataHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDataHandler.class);
    public static final int MAX_MOST_POS = 100;
    public static final int MAX_MOST_LEMMA = 20;
    public static final int MAX_MOST_PHRASE_CHUNK = 15;
    public static final int MAX_MOST_SHORTEST_PATH = 100;
    public static final int MAX_MOST_CONCEPT_TYPE = 100;
    private static SimilarityDataHandler dataHandler = null;
    private static List<SimilaritySequence> sourceSequences = Collections.emptyList();

    public static SimilarityDataHandler getInstance() {
        if (dataHandler == null) {
            dataHandler = new SimilarityDataHandler();
        }
        return dataHandler;
    }

    public SimilarityDataHandler() {
        SimilarityDataHandler.sourceSequences = loadSequences("trainSequences.data");

    }

    public List<SimilaritySequence> generateSequences(List<Relation> relations) {
        List<SimilaritySequence> sequences = new ArrayList<SimilaritySequence>();
        for (Relation relation : relations) {
            SimilaritySequence data = new SimilaritySequence();
            data.setPosTagSequences(DataPreprocess.posSequence(relation));
            data.setLemmaSequences(DataPreprocess.lemmaSequence(relation));
            data.setShortestPaths(DataPreprocess.shortestPath(relation));
            data.setPhraseChunks(DataPreprocess.phraseChunkSequence(relation));
            data.setRelType(relation.getType());
            sequences.add(data);
        }
        LOG.info("Build Sequences success!");
        return sequences;
    }

    public void saveSequences(List<SimilaritySequence> sequences, String filePath) {
        try {
            FileOutputStream fout = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(sequences);
            oos.close();
            fout.close();
            LOG.info("Save sequences to file '" + filePath + "' success!");
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<SimilaritySequence> loadSequences(String filePath) {
        List<SimilaritySequence> sequences = Collections.emptyList();
        try {

            FileInputStream fin = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            sequences = (List<SimilaritySequence>) ois.readObject();
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Load sequences Success! Total: " + sequences.size());
        return sequences;
    }

    public double[] statisticPosDistance(SimilaritySequence curSequence) {
        DataStatistic dataStatistic = null;
        Queue<DataObject> q = new PriorityQueue<DataObject>();
        for (SimilaritySequence sequence : SimilarityDataHandler.sourceSequences) {
            if (!sequence.equals(curSequence)) {
                int distance = LevenshteinDistance.computeDistance(sequence.getPosTagSequences(),
                        curSequence.getPosTagSequences());
                if (q.size() < MAX_MOST_POS) {
                    q.add(new DataObject(distance, sequence.getRelType().getValue()));
                } else {
                    if (q.peek().getDistance() > distance) {
                        q.poll();
                        q.add(new DataObject(distance, sequence.getRelType().getValue()));
                    }
                }
            }
        }
        dataStatistic = new DataStatistic(q);
        return dataStatistic.getPercentage();
    }

    public double[] statisticLemmaDistance(SimilaritySequence curSequence) {
        DataStatistic dataStatistic = null;
        Queue<DataObject> q = new PriorityQueue<DataObject>();
        for (SimilaritySequence sequence : SimilarityDataHandler.sourceSequences) {
            if (!sequence.equals(curSequence)) {
                int distance = LevenshteinDistance.computeDistance(sequence.getLemmaSequences(),
                        curSequence.getLemmaSequences());

                if (q.size() < MAX_MOST_LEMMA) {
                    q.add(new DataObject(distance, sequence.getRelType().getValue()));
                } else {
                    if (q.peek().getDistance() > distance) {
                        q.poll();
                        q.add(new DataObject(distance, sequence.getRelType().getValue()));
                    }
                }
            }
        }
        dataStatistic = new DataStatistic(q);
        return dataStatistic.getPercentage();
    }
    
    public double[] statisticPhraseChunksDistance(SimilaritySequence curSequence) {
        DataStatistic dataStatistic = null;
        Queue<DataObject> q = new PriorityQueue<DataObject>();
        for (SimilaritySequence sequence : SimilarityDataHandler.sourceSequences) {
            if (!sequence.equals(curSequence)) {
                int distance = LevenshteinDistance.computeDistance(sequence.getPhraseChunks(),
                        curSequence.getPhraseChunks());

                if (q.size() < MAX_MOST_PHRASE_CHUNK) {
                    q.add(new DataObject(distance, sequence.getRelType().getValue()));
                } else {
                    if (q.peek().getDistance() > distance) {
                        q.poll();
                        q.add(new DataObject(distance, sequence.getRelType().getValue()));
                    }
                }
            }
        }
        dataStatistic = new DataStatistic(q);
        return dataStatistic.getPercentage();
    }
    
    public double[] statisticShortestPathDistance(SimilaritySequence curSequence) {
        DataStatistic dataStatistic = null;
        Queue<DataObject> q = new PriorityQueue<DataObject>();
        for (SimilaritySequence sequence : SimilarityDataHandler.sourceSequences) {
            if (!sequence.equals(curSequence)) {
                int distance = LevenshteinDistance.computeDistance(sequence.getShortestPaths(),
                        curSequence.getShortestPaths());

                if (q.size() < MAX_MOST_SHORTEST_PATH) {
                    q.add(new DataObject(distance, sequence.getRelType().getValue()));
                } else {
                    if (q.peek().getDistance() > distance) {
                        q.poll();
                        q.add(new DataObject(distance, sequence.getRelType().getValue()));
                    }
                }
            }
        }
        dataStatistic = new DataStatistic(q);
        return dataStatistic.getPercentage();
    }

    private class DataStatistic {
        private Queue<DataObject> dataObjects;
        private double[] percentage = new double[9];

        public DataStatistic() {
        }

        public DataStatistic(Queue<DataObject> objects) {
            this.dataObjects = objects;
            // this.type = type;
            compute();
        }

        @Override
        public String toString() {
            return String.format("0:%f|1:%f|2:%f|3:%f|4:%f|5:%f|6:%f|7:%f|8:%f", percentage[0], percentage[1],
                    percentage[2], percentage[3], percentage[4], percentage[5], percentage[6], percentage[7],
                    percentage[8]);
        }

        public double[] getPercentage() {
            return percentage;
        }

        public void setPercentage(double[] percentage) {
            this.percentage = percentage;
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

    private class DataObject implements Comparable<DataObject> {
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

    public static void main(String[] args) {
        EMRTrain2 emr = new EMRTrain2(0);
        emr.getConceptData();
        emr.getRelationData();

        // generate and save sequence form training data
        SimilarityDataHandler dataHandler = SimilarityDataHandler.getInstance();
        List<SimilaritySequence> sequences = dataHandler.generateSequences(EMRTrain2.getRelations());
        dataHandler.saveSequences(sequences, "trainSequences.data");

        // List<SimilaritySequence> source =
        // dataHandler.loadSequences("trainSequences.data");
        // double[] result =
        // dataHandler.statisticPosDistance(source.get(10050));
        // System.out.println(source.get(10050).getRelType().getValue());
        // for (double d : result) {
        // System.out.print(" " + d);
        // }
        // for (int i = 100; i < 200; i++) {
        // SimilaritySequence s = source.get(i);
        // System.out.println("=== BEGIN : " + s.getRelType());
        // for (String string : s.getLemmaSequences()) {
        // System.out.print(" " + string);
        // }
        // System.out.println();
        //
        // for (String string : s.getPosTagSequences()){
        // System.out.print(" " + string);
        // }
        // System.out.println("\n====== END =====");
        // }

    }
}
