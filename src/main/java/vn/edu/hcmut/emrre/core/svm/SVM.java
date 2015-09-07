package vn.edu.hcmut.emrre.core.svm;

import java.io.File;
import java.io.IOException;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class SVM {
    public static void main(String[] args) throws IOException {

        Problem problem = new Problem();
        problem.l = 6;
        problem.n = 2;
        // Feature[][] = new F
        Feature[][] train = { { new FeatureNode(1, 1), new FeatureNode(2, 3)},
                              { new FeatureNode(1, 2),new FeatureNode(2, 4) },
                              { new FeatureNode(1, 1),new FeatureNode(2, 5) },
                              { new FeatureNode(1, 2),new FeatureNode(2, 5) },
                              
                              { new FeatureNode(1, 3), new FeatureNode(2, 1)},
                              { new FeatureNode(1, 4),new FeatureNode(2, 2) },
                              { new FeatureNode(1, 5),new FeatureNode(2, 1) },

        };
        problem.x = train;
        // problem.x = {{new Feature(1,4),new Feature(2,4)}};
         problem.y = new double[]{1,1,1,1,2,2,2};
         
         SolverType solver = SolverType.L2R_LR; // -s 0
         double C = 1.0;    // cost of constraints violation
         double eps = 0.01; // stopping criteria

         Parameter parameter = new Parameter(solver, C, eps);
         Model model = Linear.train(problem, parameter);
         File modelFile = new File("model");
         model.save(modelFile);
         // load model or use it directly
         model = Model.load(modelFile);

         Feature[] instance = { new FeatureNode(1, 1), new FeatureNode(2, 1) };
         double prediction = Linear.predict(model, instance);
         
         System.err.println(prediction);
    }
}
