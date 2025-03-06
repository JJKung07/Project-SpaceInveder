package se233.project2.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se233.project2.view.Platform;

import java.io.IOException;

public class GameOver extends StackPane {
    public GameOver(){
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Times New Roman",50));
        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(75);
        gameOverText.setTranslateY(345);

        Button restartButton = new Button("Press spacebar to Restart Game");
        restartButton.setTextFill(Color.WHITE);
        restartButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-weight: bold; -fx-font-size: 16px;");
        restartButton.setTranslateX(75);
        restartButton.setTranslateY(420);
        restartButton.setOnAction(event -> {
            try {
                restartgame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        getChildren().addAll(gameOverText,restartButton);
    }

    public void restartgame() throws IOException {
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
}
