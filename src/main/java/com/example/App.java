package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private final int boardSize = 9;
    private final int tileSize = 80;
    private static GridPane grid;
    private static int turn = 1;
    private static int clickedRow = 0;
    private static int clickedCol = 0;
    private int[][] board = new int[boardSize][boardSize];
    private Rectangle[][] tiles = new Rectangle[boardSize][boardSize];
    private Circle[][] pieces = new Circle[boardSize][boardSize];

    @Override
    public void start(Stage stage) throws IOException {
        grid = new GridPane();
        stage.setTitle("Fianco");

        for (int row = 0; row < board.length; row++) {
            board[0][row] = 2;
            board[8][row] = 1;
            for (int col = 0; col < board[row].length; col++) {

                Rectangle tile = new Rectangle(tileSize, tileSize);
                tile.setFill(Color.MAROON);

                int finalRow = row;
                int finalCol = col;

                tile.setOnMouseClicked(event -> handleTileClick(finalRow, finalCol));
                tiles[row][col] = tile;

                grid.add(tile, col, row);

                if (row == 0 || (col == row && col < 4)) {
                    board[row][col] = 2;
                    if (col < 3) {
                        board[1 + col][7 - col] = 2;
                        board[7 - col][1 + col] = 1;
                    }
                } else if (row == 8 || (col == row && col > 4)) {
                    board[row][col] = 1;
                    if (col < 3) {
                        board[1 + col][7 - col] = 2;
                        board[7 - col][1 + col] = 1;
                    }
                } else {
                    board[row][col] = -1;
                }
            }
        }

        render();
        scene = new Scene(grid, tileSize * boardSize, tileSize * boardSize);
        stage.setScene(scene);
        stage.show();
    }

    public void render() {

        grid.getChildren().removeIf(node -> node instanceof Circle);

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                final int finalRow = row;
                final int FinalCol = col;

                if (board[row][col] == 1) {
                    Circle piece = new Circle(tileSize / 2 - 10, Color.WHITE);
                    grid.add(piece, col, row);
                    piece.setOnMouseClicked(event -> handleTileClick(finalRow, FinalCol));
                    pieces[row][col] = piece;
                } else if (board[row][col] == 2) {
                    Circle piece = new Circle(tileSize / 2 - 10, Color.BLACK);
                    grid.add(piece, col, row);
                    piece.setOnMouseClicked(event -> handleTileClick(finalRow, FinalCol));
                    pieces[row][col] = piece;
                } else {
                    pieces[row][col] = null;
                }
            }
        }
    }

    public void handleTileClick(int row, int col) {
        Paint fillColor = tiles[row][col].getFill();
        String colorAsString = ((Color) fillColor).toString();

        if (turn == 1) {
            if (colorAsString.equals(Color.LIGHTYELLOW.toString())) {
                board[clickedRow][clickedCol] = -1;
                board[row][col] = 1;
                if (col - clickedCol == -2) {
                    board[clickedRow - 1][clickedCol - 1] = -1;
                }
                if (col - clickedCol == 2) {
                    board[clickedRow - 1][clickedCol + 1] = -1;
                }
                turn = 2;
                removeHighLight();
                render();
            } else {
                Circle clickedPiece = pieces[row][col];
                if (clickedPiece != null) {
                    Paint fill = clickedPiece.getFill();
                    Color color = (Color) fill;
                    if (color.equals(Color.WHITE)) {
                        clickedRow = row;
                        clickedCol = col;

                        if (board[row - 1][col] == -1) {
                            changeTileColor(row - 1, col, Color.LIGHTYELLOW);
                        }
                        if (board[row][col - 1] == -1) {
                            changeTileColor(row, col - 1, Color.LIGHTYELLOW);
                        }
                        if (board[row][col + 1] == -1) {
                            changeTileColor(row, col + 1, Color.LIGHTYELLOW);
                        }

                        if (board[row - 1][col - 1] == 2 && board[row - 2][col - 2] == -1) {
                            changeTileColor(row - 2, col - 2, Color.LIGHTYELLOW);
                        }
                        if (board[row - 1][col + 1] == 2 && board[row - 2][col + 2] == -1) {
                            changeTileColor(row - 2, col + 2, Color.LIGHTYELLOW);
                        }
                    }
                }

            }
        } else {
            if (colorAsString.equals(Color.LIGHTYELLOW.toString())) {
                board[clickedRow][clickedCol] = -1;
                board[row][col] = 2;
                if (col - clickedCol == -2) {
                    board[clickedRow + 1][clickedCol - 1] = -1;
                }
                if (col - clickedCol == 2) {
                    board[clickedRow + 1][clickedCol + 1] = -1;
                }
                turn = 1;
                removeHighLight();
                render();
            } else {
                Circle clickedPiece = pieces[row][col];
                if (clickedPiece != null) {
                    Paint fill = clickedPiece.getFill();
                    Color color = (Color) fill;
                    if (color.equals(Color.BLACK)) {
                        clickedRow = row;
                        clickedCol = col;

                        if (board[row + 1][col] == -1) {
                            changeTileColor(row + 1, col, Color.LIGHTYELLOW);
                        }
                        if (board[row][col - 1] == -1) {
                            changeTileColor(row, col - 1, Color.LIGHTYELLOW);
                        }
                        if (board[row][col + 1] == -1) {
                            changeTileColor(row, col + 1, Color.LIGHTYELLOW);
                        }

                        if (board[row + 1][col - 1] == 1 && board[row + 2][col - 2] == -1) {
                            changeTileColor(row + 2, col - 2, Color.LIGHTYELLOW);
                        }
                        if (board[row + 1][col + 1] == 1 && board[row + 2][col + 2] == -1) {
                            changeTileColor(row + 2, col + 2, Color.LIGHTYELLOW);
                        }
                    }
                }

            }
        }

    }

    private void changeTileColor(int row, int col, Color color) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            Rectangle tile = tiles[row][col];
            tile.setFill(color);
        }
    }

    private void removeHighLight() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Rectangle tile = tiles[row][col];
                tile.setFill(Color.MAROON);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }

}