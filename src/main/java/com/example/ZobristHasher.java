package com.example;

import java.util.Random;

class ZobristHasher {
    private long[][][] zobristTable;
    private int boardSize;
    private Random random;

    public ZobristHasher(int boardSize) {
        this.boardSize = boardSize;
        random = new Random();

        zobristTable = new long[boardSize][boardSize][3];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                for (int pieceType = 0; pieceType < 3; pieceType++) {
                    zobristTable[row][col][pieceType] = random.nextLong();
                }
            }
        }
    }

    public long computeHash(int[][] board) {
        long hash = 0L;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int piece = board[row][col];
                int pieceIndex = 0;
                if (piece == 1) {
                    pieceIndex = 1;
                } else if (piece == 2) {
                    pieceIndex = 2;
                }
                hash ^= zobristTable[row][col][pieceIndex];
            }
        }
        return hash;
    }
}
