package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SlangWord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String word;
    private List<String> definitions;
    
    public SlangWord(String word, List<String> definitions) {
        this.word = word;
        this.definitions = new ArrayList<>(definitions);
    }
    
    // Getters and setters
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    
    public List<String> getDefinitions() { return definitions; }
    public void setDefinitions(List<String> definitions) { 
        this.definitions = new ArrayList<>(definitions); 
    }
    
    public void addDefinition(String definition) {
        this.definitions.add(definition);
    }
    
    @Override
    public String toString() {
        return word + ": " + String.join(" | ", definitions);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SlangWord other = (SlangWord) obj;
        return word.equalsIgnoreCase(other.word);
    }
    
    @Override
    public int hashCode() {
        return word.toLowerCase().hashCode();
    }
}