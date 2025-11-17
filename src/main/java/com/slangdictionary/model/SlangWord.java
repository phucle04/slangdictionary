package com.slangdictionary.model;

public class SlangWord {
    private String word;
    private String definition;

    public SlangWord(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    // Getters and setters
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return word + ": " + definition;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SlangWord slangWord = (SlangWord) obj;
        return word.equalsIgnoreCase(slangWord.word);
    }

    @Override
    public int hashCode() {
        return word.toLowerCase().hashCode();
    }
}