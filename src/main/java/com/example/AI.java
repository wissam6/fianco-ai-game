package com.example;

import java.util.List;
import java.util.ArrayList;

public class AI {

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
