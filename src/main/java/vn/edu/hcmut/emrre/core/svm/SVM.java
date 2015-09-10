package vn.edu.hcmut.emrre.core.svm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;
import edu.stanford.nlp.*;
import edu.stanford.nlp.dcoref.CoNLL2011DocumentReader.NamedEntityAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class SVM {
    public static void main(String[] args) throws IOException {

    	//nlp
    	PrintWriter out;
		out = new PrintWriter(System.out);
		Properties props = new Properties();
		props.setProperty("annotators",	"tokenize, ssplit, pos");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation("She presented with. Hello,");
		pipeline.annotate(annotation);
		//pipeline.prettyPrint(annotation, out);
		
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    int i = 0;
	    System.out.print(sentences.size());
		for (CoreMap sentence:sentences){
	    	System.out.println("sentences " + i++);
	    	System.out.println((sentence.get(TokensAnnotation.class)).size());
	    	for (CoreLabel token:sentence.get(TokensAnnotation.class)){
	    		String word = token.get(TextAnnotation.class);
	    		//String pos = token.get(PartOfSpeechAnnotation.class);
	    		//String lemma = token.get(LemmaAnnotation.class);
	    		//String ner = token.get(NamedEntityTagAnnotation.class); 
	    		//System.out.println(word + " " + ner);
	    	}
	    	//Tree tree = sentence.get(TreeAnnotation.class);
	    	//SemanticGraph tree = sentence.get(BasicDependenciesAnnotation.class);

	    	//System.out.println(tree);
	    	
	    }
    	
		//svm
//        Problem problem = new Problem();
//        problem.l = 8;
//        problem.n = 2;
//        // Feature[][] = new F
//        Feature[][] train = { { new FeatureNode(1, 2), new FeatureNode(2, 3)},
//                              { new FeatureNode(1, 2),new FeatureNode(2, 2) },
//                              { new FeatureNode(1, 3),new FeatureNode(2, 2) },
//                              { new FeatureNode(1, 3),new FeatureNode(2, 3) },
//                              { new FeatureNode(1, 1), new FeatureNode(2, 4)},
//                              { new FeatureNode(1, 1),new FeatureNode(2, 1) },
//                              { new FeatureNode(1, 4),new FeatureNode(2, 4) },
//                              { new FeatureNode(1, 4),new FeatureNode(2, 1) }
//
//        };
//        problem.x = train;
//        // problem.x = {{new Feature(1,4),new Feature(2,4)}};
//         problem.y = new double[]{1,1,1,1,-1,-1,-1,-1};
//         
//         SolverType solver = SolverType.MCSVM_CS; // -s 0
//         double C = 1.0;    // cost of constraints violation
//         double eps = 0.01; // stopping criteria
//
//         Parameter parameter = new Parameter(solver, C, eps);
//         Model model = Linear.train(problem, parameter);
//         File modelFile = new File("model");
//         model.save(modelFile);
//         // load model or use it directly
//         model = Model.load(modelFile);
//
//         Feature[] instance = { new FeatureNode(1, 2), new FeatureNode(2, 2) };
//         double prediction = Linear.predict(model, instance);
//         
//         System.err.println(prediction);
    }
}
