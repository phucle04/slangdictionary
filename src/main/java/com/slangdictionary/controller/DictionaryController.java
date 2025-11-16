package com.slangdictionary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DictionaryController {

    @FXML
    private TextField txtInput;

    @FXML
    private TextArea txtResult;

    @FXML
    public void initialize() {
        System.out.println("✅ Controller initialized!");
        txtResult.setText("Welcome to Slang Dictionary! 🎯\n\n" +
                "Enter a slang word or definition to search...");
    }

    // Các phương thức xử lý sự kiện
    @FXML
    private void onConfirm() {
        String input = txtInput.getText();
        txtResult.setText("Confirm clicked!\nInput: " + input + "\n\nFeature coming soon...");
    }

    @FXML
    private void onSearch() {
        txtResult.setText("Search by key clicked!\n\nFeature coming soon...");
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
        txtResult.setText("Reset clicked!\n\nFeature coming soon...");
    }

    @FXML
    private void onAdd() {
        txtResult.setText("ADD clicked!\n\nFeature coming soon...");
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