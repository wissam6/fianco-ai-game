package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
// import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        /*
         * scene = new Scene(loadFXML("primary"), 640, 480);
         * stage.setScene(scene);
         * stage.show();
         */
        stage.setTitle("Fianco");
        String[][] table = new String[9][9];
        GridPane gridPane = new GridPane();

        // add initial pieces
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                Button button = new Button();
                // button.setStyle("-fx-background-color: #5c4300; ");
                if (j == 0 || (i == j && i < 4)) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.BLACK);
                    button.setGraphic(circle);
                } else if (j == 8 || (i == j && i > 4)) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.WHITE);
                    button.setGraphic(circle);
                }

                // black
                if (i == 7 && j == 1) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.BLACK);
                    button.setGraphic(circle);
                }
                if (i == 6 && j == 2) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.BLACK);

                    button.setGraphic(circle);
                }
                if (i == 5 && j == 3) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.BLACK);
                    button.setGraphic(circle);
                }

                // white
                if (i == 1 && j == 7) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.WHITE);
                    button.setGraphic(circle);
                }
                if (i == 2 && j == 6) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.WHITE);
                    button.setGraphic(circle);
                }
                if (i == 3 && j == 5) {
                    Circle circle = new Circle(100.0f, 100.0f, 20.f);
                    circle.setFill(javafx.scene.paint.Color.WHITE);
                    button.setGraphic(circle);
                }

                button.setPrefSize(100, 100);

                gridPane.add(button, i, j);
            }
        }

        Scene scene = new Scene(gridPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}