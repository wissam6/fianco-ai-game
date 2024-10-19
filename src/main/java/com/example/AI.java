package com.example;

import java.util.List;
import java.util.ArrayList;

public class AI {

    private ZobristHasher hasher;
    private TranspositionTable transTable;
    private int boardSize;

    public AI(int boardSize) {
        this.boardSize = boardSize;
        hasher = new ZobristHasher(boardSize);
        transTable = new TranspositionTable();
    }

    public BestMove negaMax(int[][] board, int depth, int alpha, int beta, int color, int player) {
        long hash = hasher.computeHash(board);
        TableEntry entry = transTable.lookup(hash);

        if (entry != null && entry.depth >= depth) {
            if (entry.flag == 0) {
                return new BestMove(entry.evaluation, entry.bestMove);
            } else if (entry.flag == -1 && entry.evaluation <= alpha) {
                return new BestMove(alpha, entry.bestMove);
            } else if (entry.flag == 1 && entry.evaluation >= beta) {
                return new BestMove(beta, entry.bestMove);
            }
        }

        if (depth == 0) {
            return new BestMove(color * evaluateBoard(board, player), null);
        }

        int alphaOrig = alpha;
        int maxEval = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> possibleMoves = generateMoves(board, color, player);
        for (Move move : possibleMoves) {

            if (Math.abs(move.fromRow - move.toRow) == 2) {
                makeMove(board, move, player);
                BestMove eval = negaMax(board, depth - 1, -beta, -alpha, color, player);
                undoMove(board, move, player);
                int currentEval = -eval.score;
                if (currentEval > maxEval) {
                    maxEval = currentEval;
                    bestMove = move;
                }
                break;
            } else {
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
        }

        int flag;
        if (maxEval <= alphaOrig) {
            flag = 1;
        } else if (maxEval >= beta) {
            flag = -1;
        } else {
            flag = 0;
        }

        transTable.store(hash, new TableEntry(maxEval, depth, flag, bestMove));

        return new BestMove(maxEval, bestMove);
    }

    public int evaluateBoard(int[][] board, int player) {
        int score = 0;
        int boardSize = board.length;
        int aiPlayer = player;
        int opponentPlayer = (player == 1) ? 2 : 1;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < board[row].length; col++) {
                int piece = board[row][col];

                if (piece == aiPlayer) {

                    int advancement = (aiPlayer == 1) ? (boardSize - 1 - row) : row;
                    score += advancement * 10;

                    if ((aiPlayer == 1 && row == 1) || (aiPlayer == 2 && row == boardSize - 2)) {
                        score += 50;
                    }

                    if ((aiPlayer == 1 && row == 0) || (aiPlayer == 2 && row == boardSize - 1)) {
                        score += 1000;
                    }

                } else if (piece == opponentPlayer) {

                    int opponentAdvancement = (aiPlayer == 1) ? row : (boardSize - 1 - row);
                    score -= opponentAdvancement * 10;

                    if ((aiPlayer == 1 && row == boardSize - 2) || (aiPlayer == 2 && row == 1)) {
                        score -= 50;
                    }

                    if ((aiPlayer == 1 && row == boardSize - 1) || (aiPlayer == 2 && row == 0)) {
                        score -= 1000;
                    }
                }
            }
        }

        return score;
    }

    public List<Move> generateMoves(int[][] board, int color, int currentPlayer) {

        List<Move> captureMoves = new ArrayList<>();
        List<Move> normalMoves = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {

                if (board[row][col] == currentPlayer) {
                    List<Move> validMoves = getValidMoves(board, row, col, currentPlayer);
                    for (Move move : validMoves) {
                        if (Math.abs(move.fromRow - move.toRow) == 2) {
                            captureMoves.add(move);
                        } else {
                            normalMoves.add(move);
                        }
                    }
                }
            }
        }

        if (!captureMoves.isEmpty()) {
            return captureMoves;
        } else {
            return normalMoves;
        }
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
