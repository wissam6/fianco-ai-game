package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private final int boardSize = 9;
    private final int tileSize = 80;
    private static GridPane grid;
    private int[][] board = new int[boardSize][boardSize];

    @Override
    public void start(Stage stage) throws IOException {
        grid = new GridPane();
        stage.setTitle("Fianco");

        for (int row = 0; row < board.length; row++) {
            board[0][row] = 1;
            board[8][row] = 2;
            for (int col = 0; col < board[row].length; col++) {

                Rectangle tile = new Rectangle(tileSize, tileSize);
                tile.setFill(Color.MAROON);
                grid.add(tile, col, row);

                if (row == 0 || (col == row && col < 4)) {
                    board[row][col] = 1;
                    if (col < 3) {
                        board[1 + col][7 - col] = 1;
                        board[7 - col][1 + col] = 2;
                    }
                } else if (row == 8 || (col == row && col > 4)) {
                    board[row][col] = 2;
                }
            }
        }

        render();
        scene = new Scene(grid, tileSize * boardSize, tileSize * boardSize);
        stage.setScene(scene);
        stage.show();
    }

    public void render() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == 1) {
                    Circle piece = new Circle(tileSize / 2 - 10, Color.BLACK);
                    grid.add(piece, col, row);
                } else if (board[row][col] == 2) {
                    Circle piece = new Circle(tileSize / 2 - 10, Color.WHITE);
                    grid.add(piece, col, row);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }

}