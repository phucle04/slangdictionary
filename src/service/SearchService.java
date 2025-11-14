package service;

import model.SlangWord;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    
    // Tìm kiếm chính xác slang word
    public SlangWord searchExactSlangWord(String word, List<SlangWord> dictionary) {
        long startTime = System.currentTimeMillis();
        
        SlangWord result = dictionary.stream()
                .filter(sw -> sw.getWord().equalsIgnoreCase(word))
                .findFirst()
                .orElse(null);
        
        long endTime = System.currentTimeMillis();
        System.out.println("Search time: " + (endTime - startTime) + "ms");
        
        return result;
    }
    
    // Tìm kiếm slang words bắt đầu với prefix
    public List<SlangWord> searchByPrefix(String prefix, List<SlangWord> dictionary) {
        return dictionary.stream()
                .filter(sw -> sw.getWord().toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Tìm kiếm slang words chứa keyword
    public List<SlangWord> searchContains(String keyword, List<SlangWord> dictionary) {
        return dictionary.stream()
                .filter(sw -> sw.getWord().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Tìm kiếm theo definition
    public List<SlangWord> searchByDefinition(String keyword, List<SlangWord> dictionary) {
        return dictionary.stream()
                .filter(sw -> sw.getDefinitions().stream()
                        .anyMatch(def -> def.toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }
}