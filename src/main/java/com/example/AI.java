package com.example;

import java.util.List;
import java.util.ArrayList;

public class AI {
    private int boardSize;

    public AI() {
        this.boardSize = 9;
    }

    public int evaluateBoard(int[][] board) {
        int score = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == 1) {
                    score += 1;
                } else if (board[row][col] == 2) {
                    score -= 1;
                }
            }
        }
        return score;
    }

    // color is +1 or -1
    public int negaMax(int[][] board, int depth, int alpha, int beta, int color) {

        if (depth == 0) {
            return color * evaluateBoard(board);
        }

        int maxEval = Integer.MIN_VALUE;

        List<Move> possibleMoves = generateMoves(board, color);

        for (Move move : possibleMoves) {

            makeMove(board, move);

            int eval = -negaMax(board, depth - 1, -beta, -alpha, -color);

            undoMove(board, move);

            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);

            if (alpha >= beta) {
                break;
            }
        }
        // System.out.println("maxEval" + maxEval);
        return maxEval;
    }

    public List<Move> generateMoves(int[][] board, int color) {
        List<Move> moves = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == color) {
                    moves.addAll(getValidMoves(board, row, col));
                }
            }
        }
        return moves;
    }

    public void makeMove(int[][] board, Move move) {
        board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol];
        board[move.fromRow][move.fromCol] = -1;
    }

    public void undoMove(int[][] board, Move move) {
        board[move.fromRow][move.fromCol] = board[move.toRow][move.toCol];
        board[move.toRow][move.toCol] = -1;
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

    public List<Move> getValidMoves(int[][] board, int row, int col) {
        List<Move> validMoves = new ArrayList<>();
        if (row > 0 && board[row - 1][col] == -1) {
            validMoves.add(new Move(row, col, row - 1, col));
        }
        return validMoves;
    }
}
