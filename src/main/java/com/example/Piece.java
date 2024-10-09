package com.example;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Piece {
    private Circle piece;

    public Piece(int size, Color color) {
        this.piece = new Circle(size, color);
    }

    public Circle getPiece() {
        return piece;
    }

    public Paint getFill() {
        return piece.getFill();
    }

    public void setFill(Paint color) {
        piece.setFill(color);
    }
}
