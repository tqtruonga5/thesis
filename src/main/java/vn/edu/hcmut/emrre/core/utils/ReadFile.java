package vn.edu.hcmut.emrre.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.DocLine;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.io.DataReader;

public class ReadFile {
    
    private String folder;
    
    public String getFolder(String folder){
        return this.folder;
    }
    
    public void setFolder(String folder){
        this.folder = folder;
    }
    
    public List<Relation> getAllRelation(List<Concept> concepts, boolean noneRelation){
        List<Relation> relations = new ArrayList<Relation>();
        File folder = new File("src/main/resources/" + this.folder + "/rel");
        File[] files = folder.listFiles();
        DataReader readFile = new DataReader();
        
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){
                relations.addAll(readFile.readRelations(concepts, this.folder + "/rel/" + files[i].getName()));
            }
        }
        if (noneRelation){
            for (int i = 0; i < concepts.size() - 1; i++)
                for (int j = i + 1; j < concepts.size(); j++) {
                    if (Relation.canRelate(concepts.get(i), concepts.get(j))){
                        if (!Relation.hasRelation(concepts.get(i), concepts.get(j))) {
                            relations.add(new Relation(concepts.get(i).getFileName(), concepts.get(i).getKey(), concepts.get(j).getKey(),
                                    Relation.Type.NONE, relations.size()));
                        }
                    }
                }
        }
        for (int i = 0; i < relations.size() - 1; i++)
        {
                Concept first = concepts.get(relations.get(i).getPreConcept());
                Concept second = concepts.get(relations.get(i).getPosConcept());
                if (!Relation.canRelate(first, second)){
                    relations.remove(i);
                }
        }
        return relations;
    }
    
    public List<Concept> getAllConcept(int startIndex){
        List<Concept> concepts = new ArrayList<Concept>();
        File folder = new File("src/main/resources/" + this.folder + "/concept");
        File[] files = folder.listFiles();
        DataReader readFile = new DataReader();
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){
                concepts.addAll(readFile.readConcepts(this.folder + "/concept/" + files[i].getName(), startIndex + concepts.size()));
            }
        }
        return concepts;
    }
    
    public List<DocLine> getAllDocLine(){
        List<DocLine> doclines = new ArrayList<DocLine>();
        File folder = new File("src/main/resources/" + this.folder + "/txt");
        File[] files = folder.listFiles();
        DataReader readFile = new DataReader();
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){
                System.out.println(files[i].getPath());
                doclines.addAll(readFile.readDocument(this.folder + "/txt/" + files[i].getName()));
            }
        }
        return doclines;
    }
    
    public static void main(String[] args){
        ReadFile readfile = new ReadFile();
        readfile.setFolder("i2b2data/train/partners");
        List<Concept> concepts = readfile.getAllConcept(0);
        System.out.println(concepts.size());
        List<Relation> relations = readfile.getAllRelation(concepts, true);
        System.out.println("Total :" + relations.size());
        System.out.println(concepts.get(concepts.size() - 1).getKey());
        
        
    }
}
