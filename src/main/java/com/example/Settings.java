package com.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Settings {
    private Stage stage = new Stage();
    private static Scene scene;
    private final int boardSize = 9;
    private final int tileSize = 82;
    private Label moveLabel = new Label("player1: a-0");
    private Label moveLabel2 = new Label("player2: a-0");

    public Scene getScene() {
        stage.setTitle("Player Selection");

        Label playerOneLabel = new Label("Player One:");
        Label playerTwoLabel = new Label("Player Two:");

        playerOneLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        playerTwoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ComboBox<String> playerOneComboBox = new ComboBox<>();
        playerOneComboBox.getItems().addAll("ai", "person");
        playerOneComboBox.setValue("person");
        playerOneComboBox.setPrefWidth(100);

        ComboBox<String> playerTwoComboBox = new ComboBox<>();
        playerTwoComboBox.getItems().addAll("ai", "person");
        playerTwoComboBox.setValue("ai");
        playerTwoComboBox.setPrefWidth(100);

        Button startButton = new Button("Start Game");
        startButton.setPrefWidth(120);
        startButton.setOnAction(e -> {
            String playerOneType = playerOneComboBox.getValue();
            String playerTwoType = playerTwoComboBox.getValue();
            System.out.println("Starting game with Player One: " + playerOneType + " and Player Two: " + playerTwoType);

            startGame(playerOneType, playerTwoType);
        });

        // Create HBox for player one selection
        HBox playerOneBox = new HBox(10);
        playerOneBox.setAlignment(Pos.CENTER);
        playerOneBox.getChildren().addAll(playerOneLabel, playerOneComboBox);

        // Create HBox for player two selection
        HBox playerTwoBox = new HBox(10);
        playerTwoBox.setAlignment(Pos.CENTER);
        playerTwoBox.getChildren().addAll(playerTwoLabel, playerTwoComboBox);

        // Create the main layout
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER); // Center the content vertically
        layout.getChildren().addAll(playerOneBox, playerTwoBox, startButton);

        Scene scene = new Scene(layout, 350, 250);
        return scene;
    }

    private void startGame(String playerOneType, String playerTwoType) {
        Board mygrid = new Board(playerOneType, playerTwoType, this);
        stage.setTitle("Fianco");

        VBox labelSection = new VBox();
        labelSection.setPadding(new Insets(10));
        labelSection.setSpacing(10);

        Button undo = new Button("undo");

        undo.setOnAction(event -> {
            mygrid.undoLastMove();
        });

        labelSection.getChildren().addAll(moveLabel, moveLabel2, undo);

        HBox root = new HBox();
        root.getChildren().addAll(mygrid.getGrid(), labelSection);
        root.setSpacing(10);

        scene = new Scene(root, tileSize * boardSize + 150, tileSize * boardSize);

        stage.setScene(scene);
        stage.show();

        if (playerOneType.equals("ai")) {
            mygrid.aiMove(2, 1);
            mygrid.render();
        }
    }

    public void setMove(String move) {
        Platform.runLater(() -> moveLabel.setText("player1: " + move));
    }

    public void setMove2(String move) {
        Platform.runLater(() -> moveLabel2.setText("player2: " + move));
    }

}
