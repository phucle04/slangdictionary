package model;

import java.io.Serializable;
import java.util.*;

public class SlangDictionary implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Sử dụng HashMap để tìm kiếm nhanh
    private Map<String, SlangWord> wordMap;
    // Sử dụng TreeMap để duy trì thứ tự
    private TreeMap<String, SlangWord> sortedWordMap;
    
    public SlangDictionary() {
        this.wordMap = new HashMap<>();
        this.sortedWordMap = new TreeMap<>();
    }
    
    // Thêm slang word mới
    public boolean addSlangWord(SlangWord slangWord) {
        String key = slangWord.getWord().toLowerCase();
        if (wordMap.containsKey(key)) {
            return false; // Đã tồn tại
        }
        wordMap.put(key, slangWord);
        sortedWordMap.put(slangWord.getWord().toLowerCase(), slangWord);
        return true;
    }
    
    // Tìm kiếm theo slang word
    public SlangWord findBySlangWord(String slangWord) {
        return wordMap.get(slangWord.toLowerCase());
    }
    
    // Tìm kiếm theo definition
    public List<SlangWord> findByDefinition(String keyword) {
        List<SlangWord> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (SlangWord slangWord : wordMap.values()) {
            for (String definition : slangWord.getDefinitions()) {
                if (definition.toLowerCase().contains(lowerKeyword)) {
                    result.add(slangWord);
                    break;
                }
            }
        }
        return result;
    }
    
    // Cập nhật slang word
    public boolean updateSlangWord(String oldWord, SlangWord newSlangWord) {
        String oldKey = oldWord.toLowerCase();
        String newKey = newSlangWord.getWord().toLowerCase();
        
        if (!wordMap.containsKey(oldKey)) {
            return false;
        }
        
        // Nếu từ thay đổi, cần xóa entry cũ và thêm mới
        if (!oldKey.equals(newKey)) {
            wordMap.remove(oldKey);
            sortedWordMap.remove(oldKey);
        }
        
        wordMap.put(newKey, newSlangWord);
        sortedWordMap.put(newKey, newSlangWord);
        return true;
    }
    
    // Xóa slang word
    public boolean removeSlangWord(String slangWord) {
        String key = slangWord.toLowerCase();
        SlangWord removed = wordMap.remove(key);
        if (removed != null) {
            sortedWordMap.remove(key);
            return true;
        }
        return false;
    }
    
    // Lấy tất cả slang words (sắp xếp)
    public List<SlangWord> getAllSlangWords() {
        return new ArrayList<>(sortedWordMap.values());
    }
    
    // Lấy số lượng slang words
    public int size() {
        return wordMap.size();
    }
    
    // Kiểm tra xem slang word có tồn tại không
    public boolean contains(String slangWord) {
        return wordMap.containsKey(slangWord.toLowerCase());
    }
}