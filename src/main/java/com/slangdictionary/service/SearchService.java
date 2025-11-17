package com.slangdictionary.service;

import com.slangdictionary.model.SlangDictionary;
import com.slangdictionary.model.SlangWord;
import java.util.List;

public class SearchService {

    public List<SlangWord> search(SlangDictionary dictionary, String keyword) {
        return dictionary.searchByKeyword(keyword);
    }

    public String exactSearch(SlangDictionary dictionary, String word) {
        return dictionary.exactSearch(word);
    }
}