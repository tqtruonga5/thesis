package vn.edu.hcmut.emrre.testing;

import java.io.IOException;

import edu.stanford.nlp.time.Options.RelativeHeuristicLevel;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.svm.SVM;

public class EmrTest {
    private Double[] dataTest;
    private Relation.Type label;
    
    public EmrTest(Double[] dataTest){
        this.dataTest = dataTest;
    }
    
    public Double[] getDataTest(){
        return dataTest;
    }
    
    public void set(Double[] input){
        dataTest = input;
    }
    
    public Relation.Type getLabel(){
        return label;
    }

    public Relation.Type test() throws IOException{
        SVM svm = new SVM();
        double prediction = svm.svmTestCore(this.dataTest);
        this.label = Relation.typeOfDouble((int)prediction);
        return this.label;
    }
    //public void 
}
