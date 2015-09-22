package vn.edu.hcmut.emrre.core.svm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EmrTrain;

public class SVM {

    private String fileModel;
    private int dimension;
    private List<double[]> data;

    public SVM(String fileModel, int dimension, List<double[]> data) {
        this.fileModel = fileModel;
        this.dimension = dimension;
        this.data = data;
    }

    public String getFileModel() {
        return fileModel;
    }

    public void setModel(String fileModel) {
        this.fileModel = fileModel;
    }

    public int getDimesion() {
        return dimension;
    }

    public void setDimesion(int dimesion) {
        this.dimension = dimesion;
    }

    public List<double[]> getData() {
        return data;
    }

    public void setData(List<double[]> data) {
        this.data = data;
    }

    private Feature[] parse2FeatureNode(double[] vector, int dimension) {
        List<Feature> arrayLst = new ArrayList<Feature>();
        for (int i = 0; i < dimension; i++) {
            if (vector[i] != -1) {
                arrayLst.add(new FeatureNode(i + 1, vector[i]));
            }
        }
        Feature[] result = new FeatureNode[arrayLst.size()];
        for (int i = 0; i < arrayLst.size(); i++) {
            result[i] = arrayLst.get(i);
        }
        return result;
    }

    public void svmTrainCore() throws IOException {
        Problem svm = new Problem();
        int k;
        svm.l = data.size();
        svm.n = dimension;

        Feature[][] feature = new FeatureNode[svm.l][];
        double[] label = new double[svm.l];

        for (int i = 0; i < svm.l; i++) {
            feature[i] = parse2FeatureNode(data.get(i), dimension);
            label[i] = data.get(i)[dimension];
        }

        svm.x = feature;
        svm.y = label;
        SolverType solver = SolverType.L1R_LR; // -s 0
        double C = 1.0; // cost of constraints violation
        double eps = 0.01; // stopping criteria

        Parameter parameter = new Parameter(solver, C, eps);
        Model model = Linear.train(svm, parameter);
        File fileModel = new File(this.fileModel);
        model.save(fileModel);
        // load model or use it directly
        // model = Model.load(modelFile);
    }

    public double svmTestCore(double[] dataTest) throws IOException {
        File fileModel = new File(this.fileModel);
        Model model = Model.load(fileModel);
        Feature[] instance = parse2FeatureNode(dataTest, EmrTrain.DIMENSIONS);
        double prediction = Linear.predict(model, instance);
        return prediction;
    }

    public static void main(String[] args) throws IOException {

        EmrTrain train = new EmrTrain();
        train.readDataTraining(EmrTrain.FILE);
        int result = 0;
        int result1 = 0;
        int total = 0;
        for (int i = 0; i < EmrTrain.trainingData.size(); i++) {
            Double[] vector = EmrTrain.trainingData.get(i);
            EmrTest test = new EmrTest(vector);
            Relation.Type label = test.test();
            if (label != Relation.Type.NONE) {
                total++;
            }
            if (Relation.valueOfType(label) == vector[EmrTrain.DIMENSIONS]) {
                result++;
                if (label != Relation.Type.NONE)
                    result1++;
            }
            System.out.print("  " + Relation.valueOfType(label));
        }
        System.out.print("\nTotal true (include NONE): " + result + "/" + EmrTrain.trainingData.size());
        System.out.print("\nTotal true: " + result1 + "/" + total);
    }
}
