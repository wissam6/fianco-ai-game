package com.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
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
    Board board = new Board();
    // private AI ai = new AI();

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
        Board mygrid = new Board(playerOneType, playerTwoType, this);
        stage.setTitle("Fianco");

        VBox labelSection = new VBox();
        labelSection.setPadding(new Insets(10));
        labelSection.setSpacing(10);

        Button undo = new Button("undo");

        undo.setOnAction(event -> {
            board.undoLastMove();
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
