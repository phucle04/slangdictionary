package com.slangdictionary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.slangdictionary.model.SlangWord;
import com.slangdictionary.model.SlangDictionary;
import com.slangdictionary.service.FileService;
import java.util.List;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class DictionaryController {

    @FXML private TextField txtInput;
    @FXML private TextArea txtResult;
    @FXML private ListView<String> listViewSlang;

    private ObservableList<String> slangList;
    private SlangDictionary dictionary;
    private FileService fileService;

    @FXML
    public void initialize() {
        System.out.println("Controller initialized!");

        // Khởi tạo
        fileService = new FileService();
        slangList = FXCollections.observableArrayList();
        listViewSlang.setItems(slangList);

        // Load dữ liệu
        loadDictionary();

        // Hiển thị tất cả slang words
        displayAllSlangWords();

        txtResult.setText("Welcome to Slang Dictionary! \n\n" +
                "Total words: " + dictionary.getSize() + "\n" +
                "Select a word from list or enter to search...");
    }

    private void loadDictionary() {
        try {
            dictionary = fileService.loadSlangDictionary("data/slang.txt");
            txtResult.setText("✅ Loaded " + dictionary.getSize() + " slang words from file!\n\n" +
                    "Use search box to find specific words...");
        } catch (Exception e) {
            System.err.println("Error loading file: " + e.getMessage());
            txtResult.setText(" Loaded sample data (" + dictionary.getSize() + " words)\n\n" +
                    "File 'data/slang.txt' not found. Using sample data.\n");
        }
    }

    private void displayAllSlangWords() {
        slangList.clear();
        List<SlangWord> allWords = dictionary.getAllSlangWords();
        for (SlangWord word : allWords) {
            slangList.add(word.getWord() + " → " + word.getDefinition());
        }
    }

    @FXML
    private void onSlangSelected() {
        String selected = listViewSlang.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Lấy slang word từ chuỗi hiển thị
            String slangWord = selected.split(" → ")[0];
            String definition = dictionary.exactSearch(slangWord);

            txtResult.setText("📖 Selected Slang Word:\n\n" +
                    "Word: " + slangWord + "\n" +
                    "Definition: " + definition + "\n\n" +
                    "Use EDIT/DELETE buttons to modify this word.");
        }
    }

    @FXML
    private void onConfirm() {
        String input = txtInput.getText().trim();
        if (input.isEmpty()) {
            displayAllSlangWords();
            txtResult.setText("Displaying all " + dictionary.getSize() + " slang words");
            return;
        }

        // Search
        long startTime = System.nanoTime();
        List<SlangWord> results = dictionary.searchByKeyword(input);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; // milliseconds

        updateListView(results);

        txtResult.setText(" Search Results for: \"" + input + "\"\n\n" +
                "Found: " + results.size() + " results\n" +
                "Search time: " + duration + " ms\n\n" +
                "Click on any word to view details.");
    }

    @FXML
    private void onSearch() {
        String input = txtInput.getText().trim();
        if (input.isEmpty()) return;

        // Search với O(1)
        long startTime = System.nanoTime();
        String definition = dictionary.exactSearch(input);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        if (definition != null) {
            txtResult.setText(" Exact Match Found:\n\n" +
                    "Word: " + input + "\n" +
                    "Definition: " + definition + "\n\n" +
                    "Search time: " + duration + " ms");

            // Highlight trong list view
            highlightInListView(input);
        } else {
            txtResult.setText(" No exact match found for: " + input + "\n" +
                    "Search time: " + duration + " ms\n\n" +
                    "Try using 'Confirm' for broader search.");
        }
    }

    private void updateListView(List<SlangWord> words) {
        slangList.clear();
        for (SlangWord word : words) {
            slangList.add(word.getWord() + " → " + word.getDefinition());
        }
    }

    private void highlightInListView(String word) {
        for (int i = 0; i < slangList.size(); i++) {
            if (slangList.get(i).toLowerCase().contains(word.toLowerCase())) {
                listViewSlang.getSelectionModel().select(i);
                listViewSlang.scrollTo(i);
                break;
            }
        }
    }

    @FXML
    private void onHistory() {
        txtResult.setText("History clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onQuiz1() {
        txtResult.setText("Quiz 1 clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onQuiz2() {
        txtResult.setText("Quiz 2 clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onReset() {
        txtInput.clear();
        displayAllSlangWords();
        txtResult.setText("Reset complete!\n\nDisplaying all " + dictionary.getSize() + " slang words");
    }

    @FXML
    private void onAdd() {
        // Hiển thị dialog nhập slang word
        TextInputDialog slangDialog = new TextInputDialog();
        slangDialog.setTitle("Add New Slang Word");
        slangDialog.setHeaderText("Enter new slang word");
        slangDialog.setContentText("Slang:");

        Optional<String> slangResult = slangDialog.showAndWait();
        if (slangResult.isPresent() && !slangResult.get().trim().isEmpty()) {
            String slangWord = slangResult.get().trim();

            // Hiển thị dialog nhập definition
            TextInputDialog definitionDialog = new TextInputDialog();
            definitionDialog.setTitle("Add Definition");
            definitionDialog.setHeaderText("Enter definition for: " + slangWord);
            definitionDialog.setContentText("Definition:");

            Optional<String> definitionResult = definitionDialog.showAndWait();
            if (definitionResult.isPresent() && !definitionResult.get().trim().isEmpty()) {
                String definition = definitionResult.get().trim();
                boolean success = dictionary.addSlangWord(slangWord, definition);

                if (success) {
                    displayAllSlangWords(); // Refresh list
                    txtResult.setText("✅ Added successfully!\n\n" +
                            "Slang: " + slangWord + "\n" +
                            "Definition: " + definition + "\n\n" +
                            "Total words: " + dictionary.getSize());
                } else {
                    txtResult.setText("❌ Failed to add slang word!\n\n" +
                            "Please check that both word and definition are not empty.");
                }
            }
        }
    }

    @FXML
    private void onDelete() {
        txtResult.setText("DELETE clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onEdit() {
        txtResult.setText("EDIT clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onRandom() {
        txtResult.setText("RANDOM clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onSave() {
        txtResult.setText("SAVE clicked!\n\nFeature coming soon...");
    }
}