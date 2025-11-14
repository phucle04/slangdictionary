package service;

import model.SlangDictionary;
import model.SlangWord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final String DATA_FILE = "data/slang.dat";
    private static final String ORIGINAL_FILE = "data/slang.txt";
    
    // Load dữ liệu từ file serialized (nhanh)
    public SlangDictionary loadFromSerializedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            return (SlangDictionary) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Serialized file not found, loading from text file...");
            return loadFromTextFile();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading serialized file: " + e.getMessage());
            return loadFromTextFile();
        }
    }
    
    // Load dữ liệu từ file text gốc
    public SlangDictionary loadFromTextFile() {
        SlangDictionary dictionary = new SlangDictionary();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ORIGINAL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, dictionary);
            }
            // Lưu file serialized cho lần sau
            saveToSerializedFile(dictionary);
        } catch (IOException e) {
            System.out.println("Error loading text file: " + e.getMessage());
        }
        
        return dictionary;
    }
    
    private void processLine(String line, SlangDictionary dictionary) {
        if (line.trim().isEmpty()) return;
        
        String[] parts = line.split("`");
        if (parts.length < 2) return;
        
        String word = parts[0].trim();
        String[] definitions = parts[1].split("\\|");
        
        List<String> definitionList = new ArrayList<>();
        for (String def : definitions) {
            definitionList.add(def.trim());
        }
        
        SlangWord slangWord = new SlangWord(word, definitionList);
        dictionary.addSlangWord(slangWord);
    }
    
    // Lưu dictionary vào file serialized
    public void saveToSerializedFile(SlangDictionary dictionary) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(dictionary);
            System.out.println("Data saved to serialized file.");
        } catch (IOException e) {
            System.out.println("Error saving serialized file: " + e.getMessage());
        }
    }
    
    // Backup dữ liệu ra file text
    public void backupToTextFile(SlangDictionary dictionary, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (SlangWord slangWord : dictionary.getAllSlangWords()) {
                writer.print(slangWord.getWord() + "`");
                writer.println(String.join("| ", slangWord.getDefinitions()));
            }
            System.out.println("Data backed up to: " + filename);
        } catch (IOException e) {
            System.out.println("Error backing up data: " + e.getMessage());
        }
    }
}