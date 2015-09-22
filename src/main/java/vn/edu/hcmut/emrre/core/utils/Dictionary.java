package vn.edu.hcmut.emrre.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import vn.edu.hcmut.emrre.training.EmrTrain;

public class Dictionary {

    private static final String fileDictionary = "dictionary.txt";
    public HashMap<String, Double> dictionary;
    private double autoValue;
    
    public Dictionary(){
        this.dictionary = new HashMap<String, Double>();
        this.autoValue = 0;
    }
    
    public void addDictionay(String word){
        if (this.dictionary.get(word) == null){
            this.dictionary.put(word, autoValue++);
        }
    }
    
    public void saveDictionary2File() throws IOException{
        File file = new File(Dictionary.fileDictionary);
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(this.autoValue + "\n");
        for (String word: this.dictionary.keySet()){
            bw.write(word + " " + this.dictionary.get(word) + "\n");
        }
        bw.close();
        fw.close();
    }
    
    public void getDictionaryFromFile() throws IOException{
        File file = new File(Dictionary.fileDictionary);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        this.autoValue = Double.parseDouble(br.readLine());
        while((line = br.readLine()) != null){
            String[] str = line.split("[ ]+");
            this.dictionary.put(str[0], Double.parseDouble(str[1]));
        }
        br.close();
        fr.close();
        
    }
    
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Dictionary dictionary = new Dictionary();
        dictionary.getDictionaryFromFile();
        dictionary.addDictionay("Hello1");
        dictionary.saveDictionary2File();
        System.out.println(dictionary.autoValue);
        

    }

}
