package com.slangdictionary.model;

import java.util.*;
import java.util.stream.Collectors;

public class SlangDictionary {
    private Map<String, String> slangMap; // Cho search chính xác O(1)
    private Map<String, List<String>> definitionIndex; // Cho search theo definition

    public SlangDictionary() {
        this.slangMap = new HashMap<>();
        this.definitionIndex = new HashMap<>();
    }

    // Thêm slang word
    public void addSlang(String word, String definition) {
        String lowercaseWord = word.toLowerCase();
        slangMap.put(lowercaseWord, definition);

        // Index cho search definition
        String[] words = definition.toLowerCase().split("\\s+");
        for (String defWord : words) {
            definitionIndex.computeIfAbsent(defWord, k -> new ArrayList<>()).add(lowercaseWord);
        }
    }

    // Trong SlangDictionary.java
    public boolean addSlangWord(String word, String definition) {
        if (word == null || word.trim().isEmpty() || definition == null || definition.trim().isEmpty()) {
            return false;
        }
        String lowercaseWord = word.trim().toLowerCase();
        addSlang(lowercaseWord, definition.trim());
        return true;
    }

    // Search chính xác - O(1)
    public String exactSearch(String word) {
        return slangMap.get(word.toLowerCase());
    }

    // Search theo keyword - tối ưu cho performance
    public List<SlangWord> searchByKeyword(String keyword) {
        String lowercaseKeyword = keyword.toLowerCase();
        Set<SlangWord> results = new HashSet<>();

        // Search trong keys (slang words)
        slangMap.entrySet().stream()
                .filter(entry -> entry.getKey().contains(lowercaseKeyword))
                .forEach(entry -> results.add(new SlangWord(entry.getKey(), entry.getValue())));

        // Search trong definitions
        slangMap.entrySet().stream()
                .filter(entry -> entry.getValue().toLowerCase().contains(lowercaseKeyword))
                .forEach(entry -> results.add(new SlangWord(entry.getKey(), entry.getValue())));

        return new ArrayList<>(results);
    }

    // Lấy tất cả slang words
    public List<SlangWord> getAllSlangWords() {
        return slangMap.entrySet().stream()
                .map(entry -> new SlangWord(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public int getSize() {
        return slangMap.size();
    }

    public boolean containsWord(String word) {
        return slangMap.containsKey(word.toLowerCase());
    }

    public void removeWord(String word) {
        String lowercaseWord = word.toLowerCase();
        String definition = slangMap.get(lowercaseWord);
        if (definition != null) {
            // Remove from definition index
            String[] words = definition.toLowerCase().split("\\s+");
            for (String defWord : words) {
                List<String> wordList = definitionIndex.get(defWord);
                if (wordList != null) {
                    wordList.remove(lowercaseWord);
                    if (wordList.isEmpty()) {
                        definitionIndex.remove(defWord);
                    }
                }
            }
            // Remove from main map
            slangMap.remove(lowercaseWord);
        }
    }

    public void updateWord(String word, String newDefinition) {
        removeWord(word);
        addSlang(word, newDefinition);
    }
}