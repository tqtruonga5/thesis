package vn.edu.hcmut.emrre.main;

import java.io.IOException;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorType;
import vn.edu.hcmut.emrre.testing.EmrTest;

public class Main {
    public static void main(String[] args) throws IOException, InvalidInputDataException {
        String model = "context-model";
//        EMRTrain2 emrTrain = new EMRTrain2(FeatureExtractorType.CONTEXT);
//        emrTrain.setTrainDataFile("context-datatrain");
//        emrTrain.setModel(model);
//        emrTrain.setTwoRounds(true);
//        emrTrain.run();

        EmrTest emrTest = new EmrTest(FeatureExtractorType.CONTEXT);
        emrTest.setModel(model);
        emrTest.setTwoRounds(true);
        emrTest.run();
    }
}
