package vn.edu.hcmut.emrre.core.feature;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Relation;

public class Combine implements FeatureExtractor{

    //declare the features that combine
    private FeatureExtractor context;
    private FeatureExtractor single;
    private FeatureExtractor vicinity;
    
    private int dimensions;
    private double[] vector;
    private boolean isTrain;
    
    public Combine(){
        this.context = FeatureExtractorFactory.getInstance(FeatureExtractorType.CONTEXT);
        this.single = FeatureExtractorFactory.getInstance(FeatureExtractorType.SINGLE_CONCEPT);
        this.vicinity = FeatureExtractorFactory.getInstance(FeatureExtractorType.CONCEPT_VICINITY);
        
        this.dimensions = context.getDimension() + single.getDimension() + vicinity.getDimension();
    }

    private double[] mergeVector(List<double[]> lstVector){
        double[] result;
        int idx = 0;
        if (this.isTrain){
            result = new double[this.dimensions + 1];
            for (double[] vector:lstVector){
                for (int i = 0; i < vector.length - 1; i++)
                    result[idx++] = vector[i];
            }
            result[result.length - 1] = lstVector.get(0)[lstVector.get(0).length - 1];
        }else{
            result = new double[this.dimensions];
            for (double[] vector:lstVector)
                for (double ele:vector)
                    result[idx++] = ele;
        }
        return result;
    }
    
    public double[] buildFeatures(Relation relation) {
        this.isTrain = relation.getType() != null;
        
        //add element vector of each feature
        List<double[]> lstVector = new ArrayList<double[]>();
        lstVector.add(this.context.buildFeatures(relation));
        lstVector.add(this.single.buildFeatures(relation));
        lstVector.add(this.vicinity.buildFeatures(relation));
                
        this.vector = mergeVector(lstVector);
        return this.vector;
    }

    public int getDimension() {
        return this.dimensions;
    }

}
