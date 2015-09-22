package vn.edu.hcmut.emrre.training;

import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorFactory;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractorType;
import de.bwaldvogel.liblinear.Model;

public class EMRTrainingImpl implements EMRTranning{

    public Model trainOnContext() {
        FeatureExtractor featureExtractor = FeatureExtractorFactory.getInstance(FeatureExtractorType.CONTEXT);
        //featureExtractor.run();
        //double[] vector = featureExtractor.getVector();
        //return SVM;
        return null;
    }

    public Model trainOnWiki() {
        // TODO Auto-generated method stub
        return null;
    }

}
