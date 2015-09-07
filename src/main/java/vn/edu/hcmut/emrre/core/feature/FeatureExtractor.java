package vn.edu.hcmut.emrre.core.feature;

import vn.edu.hcmut.emrre.core.entity.DocLine;

public interface FeatureExtractor {
    double extract(DocLine sentence);
}
