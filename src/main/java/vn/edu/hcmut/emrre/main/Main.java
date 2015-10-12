package vn.edu.hcmut.emrre.main;

import java.io.IOException;

import de.bwaldvogel.liblinear.InvalidInputDataException;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorType;
import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class Main {
    public static void main(String[] args) throws IOException, InvalidInputDataException {
        String model = "file/model/combine-model";
        String dataTrainFile = "file/data-train/combine-datatrain";

//        EMRTrain2 emrTrain = new EMRTrain2(FeatureExtractorType.COMBINE);
//        emrTrain.setTrainDataFile(dataTrainFile);
//        emrTrain.setModel(model);
//        emrTrain.setTwoRounds(true);
//        emrTrain.setSparseVector(true);
//        emrTrain.run();

        EmrTest emrTest = new EmrTest(FeatureExtractorType.COMBINE);
        emrTest.setModel(model);
        emrTest.setTwoRounds(true);
        emrTest.setSparseVector(true);
        emrTest.run();
    }
}
