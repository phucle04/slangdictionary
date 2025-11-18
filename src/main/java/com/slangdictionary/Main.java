package com.slangdictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dictionary.fxml"));
            Parent root = loader.load();

            // Create scene
            Scene scene = new Scene(root, 800, 500);

            // Set up stage
            primaryStage.setTitle("Slang Dictionary");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(650);
            primaryStage.setMinHeight(350);

            // Show window
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();

            // Fallback: Create simple UI if FXML fails
            showFallbackUI(primaryStage);
        }
    }

    private void showFallbackUI(Stage primaryStage) {
        try {
            javafx.scene.control.Label label = new javafx.scene.control.Label(
                    "FXML Load Failed!\n" +
                            "Please check:\n" +
                            "1. dictionary.fxml is in src/main/resources/\n" +
                            "2. Controller class exists\n" +
                            "3. FXML file is valid"
            );
            label.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

            javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane();
            root.getChildren().add(label);

            Scene scene = new Scene(root, 400, 200);
            primaryStage.setTitle("Slang Dictionary - Fallback");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception fallbackError) {
            System.err.println("Fallback UI also failed: " + fallbackError.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Slang Dictionary with FXML...");
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}