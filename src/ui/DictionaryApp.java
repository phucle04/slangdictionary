package ui;

import model.SlangDictionary;
import model.SlangWord;
import service.FileService;
import service.SearchService;

import java.util.List;
import java.util.Scanner;

public class DictionaryApp {
    private SlangDictionary dictionary;
    private FileService fileService;
    private SearchService searchService;
    private Scanner scanner;
    
    public DictionaryApp() {
        this.fileService = new FileService();
        this.searchService = new SearchService();
        this.scanner = new Scanner(System.in);
        loadData();
    }
    
    private void loadData() {
        System.out.println("Loading slang dictionary...");
        long startTime = System.currentTimeMillis();
        
        this.dictionary = fileService.loadFromSerializedFile();
        
        long endTime = System.currentTimeMillis();
        System.out.println("Loaded " + dictionary.size() + " slang words in " + 
                          (endTime - startTime) + "ms");
    }
    
    public void start() {
        while (true) {
            showMenu();
            int choice = getChoice();
            processChoice(choice);
        }
    }
    
    private void showMenu() {
        System.out.println("\n=== SLANG DICTIONARY ===");
        System.out.println("1. Search by Slang Word");
        System.out.println("2. Search by Definition");
        System.out.println("3. Show Search History");
        System.out.println("4. Add New Slang Word");
        System.out.println("5. Edit Slang Word");
        System.out.println("6. Delete Slang Word");
        System.out.println("7. Reset Original Data");
        System.out.println("8. Random Slang Word");
        System.out.println("9. Quiz Game - Find Definition");
        System.out.println("10. Quiz Game - Find Slang Word");
        System.out.println("11. Show All Slang Words");
        System.out.println("12. Backup Data");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void processChoice(int choice) {
        switch (choice) {
            case 1:
                searchBySlangWord();
                break;
            case 2:
                searchByDefinition();
                break;
            case 3:
                showHistory();
                break;
            case 4:
                addSlangWord();
                break;
            case 5:
                editSlangWord();
                break;
            case 6:
                deleteSlangWord();
                break;
            case 7:
                resetData();
                break;
            case 8:
                showRandomSlangWord();
                break;
            case 9:
                quizFindDefinition();
                break;
            case 10:
                quizFindSlangWord();
                break;
            case 11:
                showAllSlangWords();
                break;
            case 12:
                backupData();
                break;
            case 0:
                exit();
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }
    
    private void searchBySlangWord() {
        System.out.print("Enter slang word to search: ");
        String word = scanner.nextLine().trim();
        
        if (word.isEmpty()) {
            System.out.println("Please enter a valid slang word.");
            return;
        }
        
        SlangWord result = dictionary.findBySlangWord(word);
        
        if (result != null) {
            System.out.println("\nFound: " + result);
        } else {
            System.out.println("Slang word '" + word + "' not found.");
            
            // Tìm kiếm gần đúng
            List<SlangWord> similar = searchService.searchContains(word, 
                    dictionary.getAllSlangWords());
            if (!similar.isEmpty()) {
                System.out.println("Similar slang words:");
                similar.forEach(sw -> System.out.println("  - " + sw.getWord()));
            }
        }
    }
    
    private void searchByDefinition() {
        System.out.print("Enter keyword to search in definitions: ");
        String keyword = scanner.nextLine().trim();
        
        if (keyword.isEmpty()) {
            System.out.println("Please enter a valid keyword.");
            return;
        }
        
        List<SlangWord> results = dictionary.findByDefinition(keyword);
        
        if (!results.isEmpty()) {
            System.out.println("\nFound " + results.size() + " slang words:");
            results.forEach(sw -> System.out.println("  - " + sw));
        } else {
            System.out.println("No slang words found with definition containing: " + keyword);
        }
    }
    
    private void showHistory() {
        // Implementation for search history
        System.out.println("Search history feature coming soon...");
    }
    
    private void addSlangWord() {
        System.out.print("Enter new slang word: ");
        String word = scanner.nextLine().trim();
        
        if (word.isEmpty()) {
            System.out.println("Slang word cannot be empty.");
            return;
        }
        
        if (dictionary.contains(word)) {
            System.out.println("Slang word already exists!");
            return;
        }
        
        System.out.print("Enter definitions (separate multiple definitions with '|'): ");
        String definitionsInput = scanner.nextLine().trim();
        
        if (definitionsInput.isEmpty()) {
            System.out.println("At least one definition is required.");
            return;
        }
        
        String[] definitions = definitionsInput.split("\\|");
        List<String> definitionList = new ArrayList<>();
        for (String def : definitions) {
            definitionList.add(def.trim());
        }
        
        SlangWord newSlangWord = new SlangWord(word, definitionList);
        
        if (dictionary.addSlangWord(newSlangWord)) {
            fileService.saveToSerializedFile(dictionary);
            System.out.println("Slang word added successfully!");
        } else {
            System.out.println("Failed to add slang word.");
        }
    }
    
    private void editSlangWord() {
        System.out.print("Enter slang word to edit: ");
        String oldWord = scanner.nextLine().trim();
        
        SlangWord existing = dictionary.findBySlangWord(oldWord);
        if (existing == null) {
            System.out.println("Slang word not found!");
            return;
        }
        
        System.out.println("Current: " + existing);
        System.out.print("Enter new slang word (or press Enter to keep current): ");
        String newWord = scanner.nextLine().trim();
        
        if (newWord.isEmpty()) {
            newWord = oldWord;
        }
        
        System.out.print("Enter new definitions (or press Enter to keep current): ");
        String newDefinitionsInput = scanner.nextLine().trim();
        
        List<String> newDefinitionList;
        if (newDefinitionsInput.isEmpty()) {
            newDefinitionList = existing.getDefinitions();
        } else {
            String[] definitions = newDefinitionsInput.split("\\|");
            newDefinitionList = new ArrayList<>();
            for (String def : definitions) {
                newDefinitionList.add(def.trim());
            }
        }
        
        SlangWord updatedSlangWord = new SlangWord(newWord, newDefinitionList);
        
        if (dictionary.updateSlangWord(oldWord, updatedSlangWord)) {
            fileService.saveToSerializedFile(dictionary);
            System.out.println("Slang word updated successfully!");
        } else {
            System.out.println("Failed to update slang word.");
        }
    }
    
    private void deleteSlangWord() {
        System.out.print("Enter slang word to delete: ");
        String word = scanner.nextLine().trim();
        
        SlangWord existing = dictionary.findBySlangWord(word);
        if (existing == null) {
            System.out.println("Slang word not found!");
            return;
        }
        
        System.out.println("Are you sure you want to delete: " + existing);
        System.out.print("Enter 'YES' to confirm: ");
        String confirmation = scanner.nextLine().trim();
        
        if ("YES".equalsIgnoreCase(confirmation)) {
            if (dictionary.removeSlangWord(word)) {
                fileService.saveToSerializedFile(dictionary);
                System.out.println("Slang word deleted successfully!");
            } else {
                System.out.println("Failed to delete slang word.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void resetData() {
        System.out.print("Are you sure you want to reset to original data? (YES/NO): ");
        String confirmation = scanner.nextLine().trim();
        
        if ("YES".equalsIgnoreCase(confirmation)) {
            this.dictionary = fileService.loadFromTextFile();
            fileService.saveToSerializedFile(dictionary);
            System.out.println("Data reset successfully!");
        } else {
            System.out.println("Reset cancelled.");
        }
    }
    
    private void showRandomSlangWord() {
        List<SlangWord> allWords = dictionary.getAllSlangWords();
        if (!allWords.isEmpty()) {
            int randomIndex = (int) (Math.random() * allWords.size());
            SlangWord randomWord = allWords.get(randomIndex);
            System.out.println("Random Slang Word: " + randomWord);
        } else {
            System.out.println("No slang words available.");
        }
    }
    
    private void quizFindDefinition() {
        // Implementation for quiz game
        System.out.println("Quiz game feature coming soon...");
    }
    
    private void quizFindSlangWord() {
        // Implementation for quiz game
        System.out.println("Quiz game feature coming soon...");
    }
    
    private void showAllSlangWords() {
        List<SlangWord> allWords = dictionary.getAllSlangWords();
        System.out.println("\nTotal slang words: " + allWords.size());
        
        System.out.print("Show all? (YES/NO): ");
        String confirmation = scanner.nextLine().trim();
        
        if ("YES".equalsIgnoreCase(confirmation)) {
            allWords.forEach(sw -> System.out.println(sw));
        }
    }
    
    private void backupData() {
        System.out.print("Enter backup filename: ");
        String filename = scanner.nextLine().trim();
        
        if (filename.isEmpty()) {
            filename = "backup_" + System.currentTimeMillis() + ".txt";
        }
        
        fileService.backupToTextFile(dictionary, "data/" + filename);
    }
    
    private void exit() {
        System.out.println("Saving data...");
        fileService.saveToSerializedFile(dictionary);
        System.out.println("Thank you for using Slang Dictionary!");
        System.exit(0);
    }
}