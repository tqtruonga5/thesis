package vn.edu.hcmut.emrre.main;

import java.io.IOException;

import vn.edu.hcmut.emrre.core.feature.FeatureExtractorType;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;
import de.bwaldvogel.liblinear.InvalidInputDataException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidInputDataException {
        String model = "similar-model";
//        EMRTrain2 emrTrain = new EMRTrain2(FeatureExtractorType.SIMILARITY);
//        emrTrain.setTrainDataFile("similar-datatrain");
//        emrTrain.setModel(model);
//        emrTrain.setTwoRounds(false);
//        emrTrain.run();

        EmrTest emrTest = new EmrTest(FeatureExtractorType.SIMILARITY);
        emrTest.setModel(model);
        emrTest.setTwoRounds(false);
        emrTest.run();
    }
}
