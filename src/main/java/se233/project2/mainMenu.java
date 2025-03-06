package se233.project2;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class mainMenu extends Region {

    public mainMenu(){

        Button startButton = new Button("Start");
        Button exitButton = new Button("Exit");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("D:\\Java\\Adv Program\\project2\\project2\\src\\main\\resources\\se233\\project2\\bg2.png", 400, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Image Logo = new Image("D:\\Java\\Adv Program\\project2\\project2\\src\\main\\resources\\se233\\project2\\logo4.png", 375, 326, false, true);
        ImageView imageViewLogo = new ImageView(Logo);
        imageViewLogo.setTranslateX(12);
        imageViewLogo.setTranslateY(150);

        setBackground(new Background(backgroundImage));

        startButton.setTranslateX(145);
        startButton.setTranslateY(485);
        startButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-weight: bold; -fx-font-size: 30px;");
        startButton.setTextFill(Color.BLACK);

        exitButton.setTranslateX(152);
        exitButton.setTranslateY(600);
        exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-weight: bold; -fx-font-size: 30px;");
        exitButton.setTextFill(Color.BLACK);

        startButton.setOnAction(event -> startGame());
        exitButton.setOnAction(event -> exitGame());
        getChildren().addAll(startButton,exitButton,imageViewLogo);
    }

    private void startGame() {
        // Create a new Scene for the game and set it in the primaryStage
        Platform platform = new Platform();
        Gameloop gameloop = new Gameloop(platform);
        drawingLoop drawingLoop = new drawingLoop(platform);

        Scene scene = new Scene(platform, platform.WIDTH, platform.HEIGHT);
        scene.setOnKeyPressed(event -> platform.getKey().add(event.getCode()));
        scene.setOnKeyReleased(event -> platform.getKey().remove(event.getCode()));

        Stage primaryStage = (Stage) getScene().getWindow();
        primaryStage.setScene(scene);

        // Start the game loops
        (new Thread(gameloop)).start();
        (new Thread(drawingLoop)).start();
    }
    private void exitGame() {
        javafx.application.Platform.exit();
    }
}
