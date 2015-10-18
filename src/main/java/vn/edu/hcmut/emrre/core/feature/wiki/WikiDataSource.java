package vn.edu.hcmut.emrre.core.feature.wiki;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class WikiDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiQuery.class);
    private static WikiDataSource dataSource;
    private Map<Integer, WikiDataWrapper> data;

    public static final String EN_WIKI_DATA_PATH = "WikiData.en";
    public static final String VN_WIKI_DATA_PATH = "WikiData.vn";

    public static final String EN_WIKI_DATA_TEST_PATH = "WikiDataTest.en";
    public static final String VN_WIKI_DATA_TEST_PATH = "WikiDataTest.vn";

    public static final int TRAIN_MODE = 1;
    public static final int TEST_MODE = 2;
    public static final int RUN_MODE = 3;

    public WikiDataSource() {
    }

    public WikiDataSource(int mode) {
        switch (mode) {
        case TRAIN_MODE:
            break;
        case TEST_MODE:
            break;
        default:
        }
    }

    public static WikiDataSource getInstance() {
        if (dataSource == null) {
            dataSource = new WikiDataSource();
        }
        return dataSource;
    }

    public void exportData(String path) {
        try {
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(data);
            oos.close();
            fout.close();
            LOGGER.info("Export Wiki data to file '" + path + "' success!");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings({ "unchecked", "resource" })
    public void loadDataTrain() {
        if (this.data == null) {
            Map<Integer, WikiDataWrapper> data = Collections.emptyMap();
            try {
                FileInputStream fin = new FileInputStream(EN_WIKI_DATA_PATH);
                ObjectInputStream ois = new ObjectInputStream(fin);
                data = (Map<Integer, WikiDataWrapper>) ois.readObject();
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            this.data = data;
            LOGGER.info("Load Wiki Data Success! Total: " + data.size());
        }

    }

    @SuppressWarnings({ "unchecked", "resource" })
    public void loadDataTest() {
        if (this.data == null) {
            Map<Integer, WikiDataWrapper> data = Collections.emptyMap();
            try {
                FileInputStream fin = new FileInputStream(EN_WIKI_DATA_TEST_PATH);
                ObjectInputStream ois = new ObjectInputStream(fin);
                data = (Map<Integer, WikiDataWrapper>) ois.readObject();
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            this.data = data;
            LOGGER.info("Load Wiki Data Test Success! Total: " + data.size());
        }

    }

    public void generateData(List<Concept> concepts, String path) {
        if (data == null) {
            data = new HashMap<Integer, WikiDataWrapper>();
        }
        int count = 0;
        for (Concept concept : concepts) {
            count++;
            WikiDataWrapper entry = new WikiDataWrapper(WikiQuery.getArticals(concept.getContent()));
            data.put(concept.getKey(), entry);
            if (count % 50 == 0) {
                System.out.println(count);
                this.exportData(path);
            }
        }
        this.exportData(path);
    }

    public Map<Integer, WikiDataWrapper> getData() {
        return data;
    }

    public void setData(Map<Integer, WikiDataWrapper> data) {
        this.data = data;
    }

    public static void main(String[] args) {
        // EMRTrain2 emrTrain2 = new EMRTrain2(0);
        // emrTrain2.getConceptData();
        // emrTrain2.getRelationData();
        // List<Relation> relations = EMRTrain2.getRelations();

        EmrTest emrTest = new EmrTest(0);
        emrTest.getConceptData();
        emrTest.generateCandidates();
        List<Relation> relations = EmrTest.candidateRelations;
        Map<Integer, Concept> map = new TreeMap<Integer, Concept>();
        for (Relation relation : relations) {
            Concept concept = relation.getPreConcept();
            if (map.get(concept.getKey()) == null) {
                map.put(concept.getKey(), concept);
            }
            concept = relation.getPosConcept();
            if (map.get(concept.getKey()) == null) {
                map.put(concept.getKey(), concept);
            }
        }

        List<Concept> concepts = new ArrayList<Concept>(map.values());
        System.out.println(concepts.size());
        WikiDataSource dataSource = new WikiDataSource();
        dataSource.generateData(concepts.subList(50, concepts.size()), WikiDataSource.EN_WIKI_DATA_TEST_PATH);
        // dataSource.loadDataTest();
        // for (Map.Entry<Integer, WikiDataWrapper> entry :
        // dataSource.getData().entrySet()) {
        // System.out.println("Id : " + entry.getKey());
        // System.out.println("Titles:" + entry.getValue().getTitles());
        // }
    }
}
