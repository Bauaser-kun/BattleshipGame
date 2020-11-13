package com.kodilla.battleshipGame.Game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Game extends Application {
    private Image imageback = new Image("file:src/main/resources/waterBackground.jpg");

    private int turnCount = 0;
    private Label turnCounter = new Label("Turn: " + turnCount);

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100,
                true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        GridPane grid = new GridPane();
        grid.add(turnCounter, 1, 1, 5, 5);
        grid.setBackground(background);
        Scene scene = new Scene(grid, 750, 750);

        primaryStage.setTitle("Game of Ships");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}