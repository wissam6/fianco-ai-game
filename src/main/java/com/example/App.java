package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private final int boardSize = 9;
    private final int tileSize = 82;
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