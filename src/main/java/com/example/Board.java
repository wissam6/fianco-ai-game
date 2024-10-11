package com.example;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.List;
import java.util.ArrayList;

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

    public Board(String playerOneType, String playerTwoType) {
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
        if (playerOne == "ai") {
            aiMove(2, 1);
            render();
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

    public void resetValidMove() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Paint fillColor = tiles[row][col].getFill();
                String colorAsString = ((Color) fillColor).toString();
                if (colorAsString.equals(Color.PURPLE.toString())) {
                    changeTileColor(row, col, Color.MAROON);
                }
            }
        }

    }

    public void aiMove(int currentPlayer, int nextPlayer) {

        int color = nextPlayer == 1 ? 1 : -1;
        BestMove bestMove = negaMax(board, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, color, nextPlayer);
        if (bestMove.move != null) {
            makeMove(board, bestMove.move, nextPlayer);
            turn = currentPlayer;
            render();

            for (int rows = 0; rows < boardSize; rows++) {
                for (int cols = 0; cols < boardSize; cols++) {
                    if (board[rows][cols] == currentPlayer) {
                        List<Move> playerMoves = getValidMoves(board, rows, cols, currentPlayer);

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
                if (col - clickedCol == -2) {
                    board[clickedRow - 1][clickedCol - 1] = -1;
                }
                if (col - clickedCol == 2) {
                    board[clickedRow - 1][clickedCol + 1] = -1;
                }
                turn = 2;

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
                        List<Move> playerOneMoves = getValidMoves(board, row, col, 1);
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
                        List<Move> playerOneMoves = getValidMoves(board, row, col, 2);
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

    private void changeTileColor(int row, int col, Color color) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            Tile tile = tiles[row][col];
            tile.setFill(color);
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

    public GridPane getGrid() {
        return grid;
    }

    public int[][] getBoard() {
        return board;
    }

    // ai
    public int evaluateBoard(int[][] board) {
        int score = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 1) {

                    score += (8 - row);

                    if (row == 1) {
                        score += 50;
                    }

                    if (row == 0) {
                        score += 1000;

                    }

                } else if (board[row][col] == 2) {

                    score -= (row * 10);

                    if (row == 7) {
                        score -= 50;
                    }

                    if (row == 6) {
                        score -= 50;
                    }

                    if (row == 8) {
                        score -= 1000;
                    }
                }
            }
        }

        return score;
    }

    public class BestMove {
        int score;
        Move move;

        public BestMove(int score, Move move) {
            this.score = score;
            this.move = move;
        }
    }

    public BestMove negaMax(int[][] board, int depth, int alpha, int beta, int color, int player) {

        if (depth == 0) {
            return new BestMove(color * evaluateBoard(board), null);
        }

        int maxEval = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> possibleMoves = generateMoves(board, color, player);
        for (Move move : possibleMoves) {

            if (move.fromRow - move.toRow == 2 || move.fromRow - move.toRow == -2) {
                BestMove eval = negaMax(board, depth - 1, -beta, -alpha, -color, player);
                return new BestMove(-eval.score, move);
            }

            makeMove(board, move, player);

            BestMove eval = negaMax(board, depth - 1, -beta, -alpha, -color, player);

            undoMove(board, move, player);

            int currentEval = -eval.score;

            if (currentEval > maxEval) {
                maxEval = currentEval;
                bestMove = move;
            }

            alpha = Math.max(alpha, currentEval);
            if (alpha >= beta) {
                break;
            }
        }

        return new BestMove(maxEval, bestMove);
    }

    public List<Move> generateMoves(int[][] board, int color, int currentPlayer) {

        List<Move> moves = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {

                if (board[row][col] == currentPlayer) {
                    moves.addAll(getValidMoves(board, row, col, currentPlayer));
                }
            }
        }
        return moves;
    }

    public void makeMove(int[][] board, Move move, int player) {
        if (player == 2) {
            board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol];
            board[move.fromRow][move.fromCol] = -1;
            if (move.fromCol - move.toCol == 2) {
                board[move.fromRow + 1][move.fromCol - 1] = -1;
            }
            if (move.fromCol - move.toCol == -2) {
                board[move.fromRow + 1][move.fromCol + 1] = -1;
            }
        } else {
            board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol];
            board[move.fromRow][move.fromCol] = -1;
            if (move.fromCol - move.toCol == 2) {
                board[move.fromRow - 1][move.fromCol - 1] = -1;
            }
            if (move.fromCol - move.toCol == -2) {
                board[move.fromRow - 1][move.fromCol + 1] = -1;
            }
        }

    }

    public void undoMove(int[][] board, Move move, int player) {
        if (player == 2) {
            board[move.fromRow][move.fromCol] = board[move.toRow][move.toCol];
            board[move.toRow][move.toCol] = -1;
            if (move.fromCol - move.toCol == 2) {
                board[move.fromRow + 1][move.fromCol - 1] = 1;
            }
            if (move.fromCol - move.toCol == -2) {
                board[move.fromRow + 1][move.fromCol + 1] = 1;
            }
        } else {
            board[move.fromRow][move.fromCol] = board[move.toRow][move.toCol];
            board[move.toRow][move.toCol] = -1;
            if (move.fromCol - move.toCol == 2) {
                board[move.fromRow - 1][move.fromCol - 1] = 2;
            }
            if (move.fromCol - move.toCol == -2) {
                board[move.fromRow - 1][move.fromCol + 1] = 2;
            }
        }

    }

    public static class Move {
        int fromRow, fromCol, toRow, toCol;

        public Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }

    public List<Move> getValidMoves(int[][] board, int row, int col, int player) {
        List<Move> validMoves = new ArrayList<>();
        if (player == 1) {
            boolean eatLeft = col > 1 && row > 1 && board[row - 1][col - 1] == 2 && board[row - 2][col - 2] == -1;
            boolean eatRight = col < 7 && row > 1 && board[row - 1][col + 1] == 2 && board[row - 2][col + 2] == -1;

            if (eatLeft) {
                validMoves.add(new Move(row, col, row - 2, col - 2));
            }

            if (eatRight) {
                validMoves.add(new Move(row, col, row - 2, col + 2));
            }

            if (!eatLeft && !eatRight) {
                if (row > 0 && board[row - 1][col] == -1) {
                    validMoves.add(new Move(row, col, row - 1, col));
                }
                if (col > 0 && board[row][col - 1] == -1) {
                    validMoves.add(new Move(row, col, row, col - 1));
                }
                if (col < 8 && board[row][col + 1] == -1) {
                    validMoves.add(new Move(row, col, row, col + 1));
                }
            }

        } else {
            boolean eatLeft = col > 1 && row < 7 && board[row + 1][col - 1] == 1 && board[row + 2][col
                    - 2] == -1;
            boolean eatRight = col < 7 && row < 7 && board[row + 1][col + 1] == 1 && board[row + 2][col
                    + 2] == -1;

            if (eatLeft) {
                validMoves.add(new Move(row, col, row + 2, col - 2));
            }

            if (eatRight) {
                validMoves.add(new Move(row, col, row + 2, col + 2));

            }
            if (!eatLeft && !eatRight) {

                if (row < 8 && board[row + 1][col] == -1) {
                    validMoves.add(new Move(row, col, row + 1, col));
                }

                if (col > 0 && board[row][col - 1] == -1) {
                    validMoves.add(new Move(row, col, row, col - 1));
                }

                if (col < 8 && board[row][col + 1] == -1) {
                    validMoves.add(new Move(row, col, row, col + 1));

                }
            }

        }

        return validMoves;
    }

}
