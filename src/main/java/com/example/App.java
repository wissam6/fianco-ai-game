package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private final int boardSize = 9;
    private final int tileSize = 80;
    public Board mygrid = new Board();

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Fianco");

        scene = new Scene(mygrid.getGrid(), tileSize * boardSize, tileSize * boardSize);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}