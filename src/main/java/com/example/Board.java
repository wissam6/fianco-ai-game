package com.example;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.List;
import java.util.Stack;

public class Board {
    private final int boardSize;
    private final int tileSize;
    private static GridPane grid;
    private static int turn;
    private static int clickedRow;
    private static int clickedCol;
    private int[][] board;
    private Piece[][] pieces;
    private Tile[][] tiles;
    private String playerOne;
    private String playerTwo;
    private Settings settings;
    private Stack<Move> moveHistory;
    AI ai = new AI();

    public Board(String playerOneType, String playerTwoType, Settings settings) {
        this.boardSize = 9;
        this.tileSize = 80;
        this.turn = 1;
        this.clickedRow = 0;
        this.clickedCol = 0;
        this.board = new int[boardSize][boardSize];
        this.grid = new GridPane();
        this.pieces = new Piece[boardSize][boardSize];
        this.tiles = new Tile[boardSize][boardSize];
        this.playerOne = playerOneType;
        this.playerTwo = playerTwoType;
        this.settings = settings;
        this.moveHistory = new Stack<>();
        initializeBoard();
        render();
    }

    public Board() {
        this.boardSize = 9;
        this.tileSize = 80;
        this.turn = 1;
        this.clickedRow = 0;
        this.clickedCol = 0;
        this.board = new int[boardSize][boardSize];
        this.grid = new GridPane();
        this.pieces = new Piece[boardSize][boardSize];
        this.tiles = new Tile[boardSize][boardSize];
        this.moveHistory = new Stack<>();
        initializeBoard();
        render();
    }

    public void initializeBoard() {
        for (int row = 0; row < board.length; row++) {
            board[0][row] = 2;
            board[8][row] = 1;
            for (int col = 0; col < board[row].length; col++) {

                Tile tile = new Tile(tileSize, Color.MAROON, Color.BLACK, 2);

                int finalRow = row;
                int finalCol = col;

                tile.getTile().setOnMouseClicked(event -> handleTileClick(finalRow, finalCol));
                tiles[row][col] = tile;

                grid.add(tile.getTile(), col, row);

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

    }

    public void render() {

        grid.getChildren().removeIf(node -> node instanceof Circle);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                final int finalRow = row;
                final int FinalCol = col;

                if (board[row][col] == 1) {
                    Piece piece = new Piece(tileSize / 2 - 10, Color.WHITE);
                    grid.add(piece.getPiece(), col, row);
                    piece.getPiece().setOnMouseClicked(event -> handleTileClick(finalRow, FinalCol));
                    pieces[row][col] = piece;
                } else if (board[row][col] == 2) {
                    Piece piece = new Piece(tileSize / 2 - 10, Color.BLACK);
                    grid.add(piece.getPiece(), col, row);
                    piece.getPiece().setOnMouseClicked(event -> handleTileClick(finalRow, FinalCol));
                    pieces[row][col] = piece;
                } else {
                    pieces[row][col] = null;
                }
            }
        }

    }

    public String moveDisplay(int row, int col) {

        char letter = (char) ('a' + col);

        int number = 9 - row;

        return String.valueOf(letter) + number;
    }

    public void resetHighLight() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Paint fillColor = tiles[row][col].getFill();
                String colorAsString = ((Color) fillColor).toString();
                if (colorAsString.equals(Color.LIGHTYELLOW.toString())) {
                    changeTileColor(row, col, Color.MAROON);
                }
            }
        }

    }

    private void removeHighLight() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Tile tile = tiles[row][col];
                tile.setFill(Color.MAROON);
            }
        }
    }

    private void changeTileColor(int row, int col, Color color) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            Tile tile = tiles[row][col];
            tile.setFill(color);
        }
    }

    public void aiMove(int currentPlayer, int nextPlayer) {

        int color = nextPlayer == 1 ? 1 : -1;
        BestMove bestMove = ai.negaMax(board, 7, Integer.MIN_VALUE, Integer.MAX_VALUE, color, nextPlayer);
        if (bestMove.move != null) {
            ai.makeMove(board, bestMove.move, nextPlayer);
            moveHistory.push(bestMove.move);

            if (nextPlayer == 1) {
                settings.setMove(moveDisplay(bestMove.move.fromRow, bestMove.move.fromCol) + "-"
                        + moveDisplay(bestMove.move.toRow, bestMove.move.toCol));
            } else {
                settings.setMove2(moveDisplay(bestMove.move.fromRow, bestMove.move.fromCol) + "-"
                        + moveDisplay(bestMove.move.toRow, bestMove.move.toCol));
            }

            turn = currentPlayer;
            render();

            for (int rows = 0; rows < boardSize; rows++) {
                for (int cols = 0; cols < boardSize; cols++) {
                    if (board[rows][cols] == currentPlayer) {
                        List<Move> playerMoves = ai.getValidMoves(board, rows, cols, currentPlayer);

                        for (Move move : playerMoves) {
                            if (move.fromRow - move.toRow == 2) {
                                changeTileColor(move.fromRow, move.fromCol, Color.PURPLE);

                            }
                            if (move.fromRow - move.toRow == -2) {
                                changeTileColor(move.fromRow, move.fromCol, Color.PURPLE);

                            }

                        }

                    }
                }
            }

        }
    }

    public void undoLastMove() {

        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            int player = board[lastMove.toRow][lastMove.toCol];
            ai.undoMove(board, lastMove, player);
            render();

        } else {
            System.out.println("move history empty");
        }
    }

    public void handleTileClick(int row, int col) {
        Paint fillColor = tiles[row][col].getFill();
        String colorAsString = ((Color) fillColor).toString();
        boolean overTake = false;
        resetHighLight();

        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                Paint fillColor1 = tiles[rows][cols].getFill();
                String colorAsString1 = ((Color) fillColor1).toString();
                if (colorAsString1.equals(Color.PURPLE.toString())) {
                    overTake = true;
                }
            }
        }

        if (turn == 1) {

            if (colorAsString.equals(Color.LIGHTYELLOW.toString())) {
                removeHighLight();

                board[clickedRow][clickedCol] = -1;
                board[row][col] = 1;
                Move moveOne = new Move(clickedRow, clickedCol, row, col);
                moveHistory.add(moveOne);
                settings.setMove(moveDisplay(clickedRow, clickedCol) + "-"
                        + moveDisplay(row, col));
                if (col - clickedCol == -2) {
                    board[clickedRow - 1][clickedCol - 1] = -1;
                }
                if (col - clickedCol == 2) {
                    board[clickedRow - 1][clickedCol + 1] = -1;
                }

                render();
                if (row == 0) {
                    System.out.println("Player 1 wins");

                }
                if (playerTwo == "ai") {
                    aiMove(1, 2);
                } else {
                    turn = 2;
                }

            } else {

                Piece clickedPiece = pieces[row][col];
                if (clickedPiece != null) {
                    Paint fill = clickedPiece.getFill();
                    Color color = (Color) fill;
                    if (color.equals(Color.WHITE)) {
                        clickedRow = row;
                        clickedCol = col;
                        List<Move> playerOneMoves = ai.getValidMoves(board, row, col, 1);
                        if (overTake) {
                            Paint fillColor2 = tiles[row][col].getFill();
                            String colorAsString2 = ((Color) fillColor2).toString();

                            if (colorAsString2.equals(Color.PURPLE.toString())) {
                                boolean eatLeft = col > 1 && row > 1 && board[row - 1][col - 1] == 2
                                        && board[row - 2][col - 2] == -1;
                                boolean eatRight = col < 7 && row > 1 && board[row - 1][col + 1] == 2
                                        && board[row - 2][col + 2] == -1;

                                if (eatLeft) {
                                    changeTileColor(row - 2, col - 2, Color.LIGHTYELLOW);
                                }

                                if (eatRight) {
                                    changeTileColor(row - 2, col + 2, Color.LIGHTYELLOW);
                                }
                            }

                        } else {
                            for (Move move : playerOneMoves) {
                                changeTileColor(move.toRow, move.toCol, Color.LIGHTYELLOW);
                            }
                        }

                    }
                }
            }
        } else {

            if (colorAsString.equals(Color.LIGHTYELLOW.toString())) {
                removeHighLight();
                board[clickedRow][clickedCol] = -1;
                board[row][col] = 2;
                Move moveTwo = new Move(clickedRow, clickedCol, row, col);
                moveHistory.add(moveTwo);
                if (col - clickedCol == -2) {

                    board[clickedRow + 1][clickedCol - 1] = -1;
                }
                if (col - clickedCol == 2) {

                    board[clickedRow + 1][clickedCol + 1] = -1;
                }
                render();

                if (row == 8) {
                    System.out.println("Player 2 wins");

                }
                if (playerOne == "ai") {
                    aiMove(2, 1);
                } else {
                    turn = 1;
                }

            } else {

                Piece clickedPiece = pieces[row][col];
                if (clickedPiece != null) {
                    Paint fill = clickedPiece.getFill();
                    Color color = (Color) fill;
                    if (color.equals(Color.BLACK)) {
                        clickedRow = row;
                        clickedCol = col;
                        List<Move> playerOneMoves = ai.getValidMoves(board, row, col, 2);
                        if (overTake) {
                            Paint fillColor2 = tiles[row][col].getFill();
                            String colorAsString2 = ((Color) fillColor2).toString();

                            if (colorAsString2.equals(Color.PURPLE.toString())) {
                                boolean eatLeft = col > 1 && row < 7 && board[row + 1][col - 1] == 1
                                        && board[row + 2][col
                                                - 2] == -1;
                                boolean eatRight = col < 7 && row < 7 && board[row + 1][col + 1] == 1
                                        && board[row + 2][col
                                                + 2] == -1;

                                if (eatLeft) {
                                    changeTileColor(row + 2, col - 2, Color.LIGHTYELLOW);
                                }

                                if (eatRight) {
                                    changeTileColor(row + 2, col + 2, Color.LIGHTYELLOW);
                                }
                            }

                        } else {
                            for (Move move : playerOneMoves) {
                                changeTileColor(move.toRow, move.toCol, Color.LIGHTYELLOW);
                            }
                        }

                    }
                }
            }
        }

    }

    public GridPane getGrid() {
        return grid;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
        render();
    }
}
