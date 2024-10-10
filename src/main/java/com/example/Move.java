package com.example;

import java.util.ArrayList;
import java.util.List;

public class Move {
    int fromRow, fromCol, toRow, toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public static List<Move> getValidMoves(int[][] board, int row, int col, int player) {
        List<Move> validMoves = new ArrayList<>();

        if (player == 1) {
            if (row > 0 && board[row - 1][col] == -1) {
                validMoves.add(new Move(row, col, row - 1, col));
            }
            if (col > 0 && row > 0 && board[row - 1][col - 1] == 2 && board[row - 2][col - 2] == -1) {
                validMoves.add(new Move(row, col, row - 2, col - 2));
            }
            if (col < 8 && row > 0 && board[row - 1][col + 1] == 2 && board[row - 2][col + 2] == -1) {
                validMoves.add(new Move(row, col, row - 2, col + 2));
            }
        } else if (player == 2) {
            if (row < 8 && board[row + 1][col] == -1) {
                validMoves.add(new Move(row, col, row + 1, col));
            }
            if (col > 0 && row < 7 && board[row + 1][col - 1] == 1 && board[row + 2][col - 2] == -1) {
                validMoves.add(new Move(row, col, row + 2, col - 2));
            }
            if (col < 8 && row < 7 && board[row + 1][col + 1] == 1 && board[row + 2][col + 2] == -1) {
                validMoves.add(new Move(row, col, row + 2, col + 2));
            }
        }
        return validMoves;
    }
}
