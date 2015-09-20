import java.io.File;
import java.io.IOException;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.io.DataReader;
import vn.edu.hcmut.emrre.training.EmrTrain;

public class Main {
 
    public static void main(String[] args) throws IOException {
    	
        File folder = new File("src/main/resources/i2b2data/train/beth/txt/");
        File[] files = folder.listFiles();
        EmrTrain train = new EmrTrain();
        DataReader dataReader = new DataReader();
        train.training();
//        System.out.println(files.length);
//        for (int i = 15; i < files.length; i++){
//            if (files[i].isFile()){
//                String name = files[i].getName().substring(0, files[i].getName().indexOf('.'));
//                String inputDocFile = "i2b2data/train/beth/txt/" + name + ".txt";
//                String inputConceptFile = "i2b2data/train/beth/concept/" + name + ".con";
//                String inputRelationFile = "i2b2data/train/beth/rel/" + name + ".rel";
//                List<DocLine> docLines = dataReader.readDocument(inputDocFile);
//                List<Concept> concepts = dataReader.readConcepts(inputConceptFile);
//                List<Relation> relations = dataReader.readRelations(concepts, inputRelationFile);
//                train.training(docLines, concepts, relations);
//            }
//        }
          
    }
}
