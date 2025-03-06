package se233.project2.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import se233.project2.controller.Keys;
import se233.project2.model.Lives;
import se233.project2.model.Score;
import se233.project2.controller.Effect;
import se233.project2.model.Boss;
import se233.project2.model.Character;
import se233.project2.model.Enemy;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends Pane {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 800;
    private final Image platformImg;
    private Image explosionImage;
    private se233.project2.model.Character character;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Lives> livesList = new ArrayList<>();
    private Keys key;
    private Map<Integer, Enemy> lastEnemies = new HashMap<>();
    private Score score = new Score();
    private Text livesText;
    private Boss boss;
    private boolean hasBoss = false;
    private boolean animationInProgress = false;

    public Platform() throws IOException {
        int numColumn = 1;
        int numRows = 1;
        key = new Keys();
        URL resourceURL = Launcher.class.getResource("/se233/project2/bg1.png");
        if (resourceURL == null) {
            throw new RuntimeException("Error: Resource bg1.png not found at path /se233/project2/bg1.png");
        }
        InputStream imgStream = resourceURL.openStream();
        platformImg = new Image(imgStream);

        ImageView backgroundImg = new ImageView(platformImg);
        backgroundImg.setFitWidth(WIDTH);
        backgroundImg.setFitHeight(HEIGHT);
        character = new se233.project2.model.Character(185,700,0,0, KeyCode.LEFT,KeyCode.RIGHT,KeyCode.SPACE,KeyCode.B);

        for(int i = 0; i < numColumn;i++){
            for (int j = 0; j < numRows;j++){
                Enemy enemy = new Enemy(1 + i * 30, 150 + j * 60,0,0);
                enemies.add(enemy);
            }
        }

        for (int i = 0; i < 3 ;i++){
            Lives live = new Lives(95 + i * 30,760,0,0);
            livesList.add(live);
        }

        for (int i = 0; i < numColumn; i++) {
            lastEnemies.put(i, null);
        }

        livesText = new Text();
        livesText.setText("Lives: ");
        livesText.setFont(Font.font("Times New Roman", 30));
        livesText.setFill(Color.WHITE);
        livesText.setX(10);
        livesText.setY(780);


        score.setLayoutX(10);
        score.setLayoutY(15);
        getChildren().addAll(backgroundImg,character,score,livesText);
        getChildren().addAll(enemies);
        getChildren().addAll(livesList);
    }
    public void showExplosionEffect(double x, double y) {
        InputStream imgStream = Launcher.class.getResourceAsStream("/se233/project2/explode.png");
        if (imgStream == null) {
            throw new RuntimeException("Resource bg1.png not found!");
        }
        explosionImage = new Image(imgStream);

        Effect explosion = new Effect(explosionImage);
        javafx.application.Platform.runLater(() ->{
            explosion.playExplosionAnimation(x, y, this,0.1);
        });
    }
    public void spawnNewEnemies() {
        System.out.println("Respawn Enemies Called");
        enemies.clear();
        int numColumn = 10;
        int numRows = 5;
        for(int i = 0; i < numColumn; i++){
            for (int j = 0; j < numRows; j++){
                Enemy enemy = new Enemy(1 + i * 30, 150 + j * 60,0,0);
                enemies.add(enemy);
            }
        }

        getChildren().addAll(enemies);
    }

    public void displayAnimationText(String text) {
        animationInProgress = true;

        Text animationText = new Text(text);
        animationText.setFont(Font.font("Times New Roman", 30));
        animationText.setFill(Color.WHITE);
        animationText.setX(WIDTH / 2 - animationText.getBoundsInLocal().getWidth() / 2);
        animationText.setY(HEIGHT / 2 - animationText.getBoundsInLocal().getHeight() / 2);
        getChildren().add(animationText);

        // Schedule the text to disappear after 3 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    getChildren().remove(animationText);
                    animationInProgress = false; // Animation has ended
                })
        );
        timeline.play();
    }
    public boolean isAnimationInProgress() {
        return animationInProgress;
    }


    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public Keys getKey() {
        return key;
    }

    public Character getCharacter() {
        return character;
    }

    public List<Enemy> getEnemy() {
        return enemies;
    }

    public List<Lives> getLivesList() {
        return livesList;
    }

    public void updateLastEnemy(int column, Enemy enemy) {
        lastEnemies.put(column, enemy);
    }
    public Enemy getLastEnemyInColumn(int column) {
        return lastEnemies.get(column);
    }
    public void increaseScore(int points) {
        score.increaseScore(points);
    }
    public boolean hasBoss() {
        return hasBoss;
    }

    public void setHasBoss(boolean hasBoss) {
        this.hasBoss = hasBoss;
    }
}
