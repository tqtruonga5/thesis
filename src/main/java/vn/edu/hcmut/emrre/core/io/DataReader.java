package vn.edu.hcmut.emrre.core.io;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;

public interface DataReader {
    Concept readSingleConcept(String line);

    List<Concept> readMultiple(String line);
    
    
}
