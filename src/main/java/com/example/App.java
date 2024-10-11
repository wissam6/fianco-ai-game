package com.example;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Settings settings = new Settings();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Settings");
        stage.setScene(settings.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}