import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import edu.stanford.nlp.util.CoreMap;
import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.feature.ContextFeature;
import vn.edu.hcmut.emrre.core.io.DataReader;
import vn.edu.hcmut.emrre.core.utils.StanfordParserHelper;
import vn.edu.hcmut.emrre.core.utils.StanfordParserHelperImpl;
import de.bwaldvogel.liblinear.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // duyet tat ca cac file
    	// 1 file
    	String inputDocFile = "i2b2data/beth/txt/record-14.txt";
        String inputConceptFile = "i2b2data/beth/concept/record-14.con";
        String inputRelationFile = "i2b2data/beth/rel/record-14.rel";
        double autoValue = 0;
        HashMap<String, Double> dictionary  = new HashMap<String, Double>();
        
        DataReader dataReader = new DataReader();
        List<DocLine> docLines = dataReader.readDocument(inputDocFile);
        List<Concept> concepts = dataReader.readConcepts(inputConceptFile);
        List<Relation> relations = dataReader.readRelations(concepts, inputRelationFile);
        //chua tat ca data training
        List<Double[][]> trainingData;
        System.out.println(relations.size());
        for (int i = 0; i < concepts.size() - 1 ; i ++)
        	for (int j = i + 1; j < concepts.size(); j++){
        		//check if there doesn't exist relation between 2 concepts
        		//add a relation NONE into relations list
        		if (!Relation.hasRelation(concepts.get(i), concepts.get(j))){
        			relations.add(new Relation(concepts.get(i).getKey(), concepts.get(j).getKey(), Relation.Type.NONE, relations.size()));
        		}
        	}
        System.out.println(relations.size());
//        preprocess
//        extract feature
//        duyet trong Concept tat ca cac cap co the 
//        for (int i = 0; i < relations.size(); i++){
//        	DocLine docLine = DocLine.getDocLine(docLines, relations.get(i).getPreConcept().getLine());
//            StanfordParserHelper stan = new StanfordParserHelperImpl();
//            CoreMap result = stan.parseDataToCoreMap(docLine.getContent(), relations.get(i));
//            System.out.println(relations.get(i).toString());
//            System.out.println(result);
//            System.out.println(ContextFeature.distance(relations.get(i)));
//            trainingData.add(ContextFeature.distance(relations.get(i)));
//        }  
//        //training
//        Problem svm = new Problem();
//        svm.l = trainingData.size();
//        svm.n = 1;
//        FeatureNode[][] train = new FeatureNode[svm.l][];
//        double[] label = new double[svm.l];
//        for (int i = 0; i < svm.l; i++){
//        	FeatureNode[] feature = new FeatureNode[]{new FeatureNode(1, trainingData.get(i))};
//        	train[i] = feature;
//        	label[i] = Relation.valueOfType(relations.get(i).getType());
//        }
//        svm.x = train;
//        svm.y = label;
//        
//        SolverType solver = SolverType.MCSVM_CS; // -s 0
//        double C = 1.0;    // cost of constraints violation
//        double eps = 0.01; // stopping criteria
//
//        Parameter parameter = new Parameter(solver, C, eps);
//        Model model = Linear.train(svm, parameter);
//        File modelFile = new File("model");
//        model.save(modelFile);
//        
//        //test
//        model = Model.load(modelFile);
//        Feature[] instance = { new FeatureNode(1, 2)};
//        double prediction = Linear.predict(model, instance); 
//        System.err.println(prediction);
//        
//        Problem svm = new Problem();
//        svm.l = 2;
//        svm.n = 4;
//        Feature[][] train= {{ new FeatureNode(1, 2), new FeatureNode(4, 3)},
//            { new FeatureNode(1, 2),new FeatureNode(2, 2),new FeatureNode(3,4) }};
//        svm.x = train;
//        svm.y = new double[]{1,-1};
//        SolverType solver = SolverType.L1R_LR; // -s 0
//        double C = 1.0;    // cost of constraints violation
//        double eps = 0.01; // stopping criteria
//
//        Parameter parameter = new Parameter(solver, C, eps);
//        Model model = Linear.train(svm, parameter);
//        File modelFile = new File("model");
//        model.save(modelFile);
       
        
    }
}
