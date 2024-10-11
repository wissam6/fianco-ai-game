package com.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Settings {
    private Stage stage = new Stage();
    private static Scene scene;
    private final int boardSize = 9;
    private final int tileSize = 82;

    public Scene getScene() {
        stage.setTitle("Player Selection");

        Label playerOneLabel = new Label("Player One:");
        Label playerTwoLabel = new Label("Player Two:");

        ComboBox<String> playerOneComboBox = new ComboBox<>();
        playerOneComboBox.getItems().addAll("ai", "person");
        playerOneComboBox.setValue("person");

        ComboBox<String> playerTwoComboBox = new ComboBox<>();
        playerTwoComboBox.getItems().addAll("ai", "person");
        playerTwoComboBox.setValue("ai");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            String playerOneType = playerOneComboBox.getValue();
            String playerTwoType = playerTwoComboBox.getValue();
            System.out.println("Starting game with Player One: " + playerOneType + " and Player Two: " + playerTwoType);

            startGame(playerOneType, playerTwoType);
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(playerOneLabel, playerOneComboBox, playerTwoLabel, playerTwoComboBox, startButton);

        Scene scene = new Scene(layout, 300, 200);
        return scene;
    }

    private void startGame(String playerOneType, String playerTwoType) {
        Board mygrid = new Board(playerOneType, playerTwoType);
        stage.setTitle("Fianco");
        scene = new Scene(mygrid.getGrid(), tileSize * boardSize, tileSize * boardSize);
        stage.setScene(scene);
        stage.show();

    }

}
