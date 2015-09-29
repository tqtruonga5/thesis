package vn.edu.hcmut.emrre.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Dictionary {

    private static final String fileDictionary = "dictionary.txt";
    private HashMap<String, Integer> dictionary;
    private int autoValue;
    
    public Dictionary(){
        this.dictionary = new HashMap<String, Integer>();
        this.autoValue = 0;
    }
    
    public void addDictionay(String word){
        if (this.dictionary.get(word) == null){
            this.dictionary.put(word, autoValue++);
        }
    }
    
    public int getValue(String word){
        return (this.dictionary.get(word) == null)? -1 : this.dictionary.get(word);
    }
    
    public int getSize(){
        return this.dictionary.size();
    }
    
    public void saveDictionary2File() throws IOException{
        File file = new File(Dictionary.fileDictionary);
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write((int)this.autoValue + "\n");
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
        this.autoValue = Integer.parseInt(br.readLine());
        while((line = br.readLine()) != null){
            String[] str = line.split("[ ]+");
            this.dictionary.put(str[0], Integer.parseInt(str[1]));
        }
        br.close();
        fr.close();
        
    }
    
    public static void main(String[] args) throws IOException {
        String word = "null";
        Dictionary dictionary = new Dictionary();
        dictionary.getDictionaryFromFile();
        System.out.println((int)dictionary.autoValue);
        dictionary.addDictionay(word);
        System.out.println((int)dictionary.autoValue);
        System.out.println(dictionary.getValue(word));
        

    }

}
