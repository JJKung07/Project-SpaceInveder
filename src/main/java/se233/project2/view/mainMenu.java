package se233.project2.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import se233.project2.controller.Gameloop;
import se233.project2.controller.drawingLoop;

import java.io.IOException;

public class mainMenu extends Region {

    public mainMenu() {
        Button startButton = new Button("Start");
        Button exitButton = new Button("Exit");
        Image bgImage, logoImage;

        try {
            // Use relative paths for resources
            bgImage = new Image(getClass().getResource("/se233/project2/bg2.png").toString(), 400, 800, false, true);
            logoImage = new Image(getClass().getResource("/se233/project2/logo4.png").toString(), 375, 326, false, true);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load resources: " + ex.getMessage());
        }

        // Set background
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        setBackground(new Background(backgroundImage));

        ImageView imageViewLogo = new ImageView(logoImage);
        imageViewLogo.setTranslateX(12);
        imageViewLogo.setTranslateY(150);

        startButton.setTranslateX(145);
        startButton.setTranslateY(485);
        startButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-weight: bold; -fx-font-size: 30px;");
        startButton.setTextFill(Color.BLACK);

        exitButton.setTranslateX(152);
        exitButton.setTranslateY(600);
        exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-weight: bold; -fx-font-size: 30px;");
        exitButton.setTextFill(Color.BLACK);

        startButton.setOnAction(event -> {
            try {
                startGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        exitButton.setOnAction(event -> exitGame());
        getChildren().addAll(startButton, exitButton, imageViewLogo);
    }

    private void startGame() throws IOException {
        // Create a new Scene for the game and set it in the primaryStage
        Platform platform = new Platform();
        Gameloop gameloop = new Gameloop(platform);
        drawingLoop drawingLoop = new drawingLoop(platform);

        Scene scene = new Scene(platform, Platform.WIDTH, Platform.HEIGHT);
        scene.setOnKeyPressed(event -> platform.getKey().add(event.getCode()));
        scene.setOnKeyReleased(event -> platform.getKey().remove(event.getCode()));

        try {
            Stage primaryStage = (Stage) getScene().getWindow();
            primaryStage.setScene(scene);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to start game: " + ex.getMessage());
        }

        // Start the game loops
        (new Thread(gameloop)).start();
        (new Thread(drawingLoop)).start();
    }

    private void exitGame() {
        javafx.application.Platform.exit();
    }
}