package vn.edu.hcmut.emrre.training;

import de.bwaldvogel.liblinear.Model;

public interface EMRTranning {
    Model trainOnContext();
    Model trainOnWiki();
    
}
