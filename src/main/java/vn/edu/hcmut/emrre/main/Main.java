package vn.edu.hcmut.emrre.main;

import java.io.IOException;

import vn.edu.hcmut.emrre.testing.EmrTest;
import vn.edu.hcmut.emrre.training.EMRTrain2;

public class Main {
    public static void main(String[] args) throws IOException {
        int type = 0;
        String model = "context";

        EMRTrain2 emrTrain = new EMRTrain2(type);
        emrTrain.setModel(model);
        emrTrain.run();

        EmrTest emrTest = new EmrTest(type);
        emrTest.setModel(model);
        emrTest.run();
    }
}
