package com.example;

import java.util.HashMap;

class TranspositionTable {
    private HashMap<Long, TableEntry> table;

    public TranspositionTable() {
        table = new HashMap<>();
    }

    public TableEntry lookup(long hash) {
        return table.get(hash);
    }

    public void store(long hash, TableEntry entry) {
        table.put(hash, entry);
    }
}

class TableEntry {
    public int evaluation;
    public int depth;
    public int flag;
    public Move bestMove;

    public TableEntry(int evaluation, int depth, int flag, Move bestMove) {
        this.evaluation = evaluation;
        this.depth = depth;
        this.flag = flag;
        this.bestMove = bestMove;
    }
}
