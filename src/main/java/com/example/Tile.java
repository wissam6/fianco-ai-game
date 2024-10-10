package com.example;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

public class Tile {
    private Rectangle tile;

    public Tile(int tileSize, Color color, Color stroke, int strokeWidth) {
        this.tile = new Rectangle(tileSize, tileSize);
        this.tile.setFill(color);
        this.tile.setStroke(stroke);
        this.tile.setStrokeWidth(strokeWidth);
    }

    public Rectangle getTile() {
        return tile;
    }

    public Paint getFill() {
        return tile.getFill();
    }

    public void setFill(Paint color) {
        tile.setFill(color);
    }

    public void fireEvent(MouseEvent event) {
        tile.fireEvent(event);
    }
}
