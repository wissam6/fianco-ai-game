package com.example;

import java.util.ArrayList;
import java.util.List;

public class AI {

    public static class BestMove {
        int score;
        Move move;

        public BestMove(int score, Move move) {
            this.score = score;
            this.move = move;
        }
    }

    public BestMove negaMax(int[][] board, int depth, int alpha, int beta, int color) {
        if (depth == 0) {
            return new BestMove(color * evaluateBoard(board), null);
        }

        int maxEval = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> possibleMoves = generateMoves(board, color);
        for (Move move : possibleMoves) {
            makeMove(board, move);
            BestMove eval = negaMax(board, depth - 1, -beta, -alpha, -color);
            undoMove(board, move);

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

    public List<Move> generateMoves(int[][] board, int color) {
        List<Move> moves = new ArrayList<>();

        int player = (color == 1) ? 1 : 2;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == player) {
                    moves.addAll(Move.getValidMoves(board, row, col, player));
                }
            }
        }
        return moves;
    }

    public void makeMove(int[][] board, Move move) {
        board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol];
        board[move.fromRow][move.fromCol] = -1;

        if (move.fromCol - move.toCol == 2) {
            board[move.fromRow + 1][move.fromCol - 1] = -1;
        } else if (move.fromCol - move.toCol == -2) {
            board[move.fromRow + 1][move.fromCol + 1] = -1;
        }
    }

    public void undoMove(int[][] board, Move move) {
        board[move.fromRow][move.fromCol] = board[move.toRow][move.toCol];
        board[move.toRow][move.toCol] = -1;

        if (move.fromCol - move.toCol == 2) {
            board[move.fromRow + 1][move.fromCol - 1] = 1;
        } else if (move.fromCol - move.toCol == -2) {
            board[move.fromRow + 1][move.fromCol + 1] = 1;
        }
    }
}
