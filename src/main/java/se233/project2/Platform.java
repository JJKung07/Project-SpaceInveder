package se233.project2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Platform extends Pane {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 800;
    private Image platformImg;
    private Character character;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Lives> livesList = new ArrayList<>();
    private Keys key;
    private Map<Integer, Enemy> lastEnemies = new HashMap<>();
    private Score score = new Score();
    private Text livesText;
    private Boss boss;
    private boolean hasBoss = false;
    private boolean animationInProgress = false;

    public Platform(){
        int numColumn = 1;
        int numRows = 1;
        key = new Keys();
        platformImg = new Image(Launcher.class.getResourceAsStream("bg1.png"));
        ImageView backgroundImg = new ImageView(platformImg);
        backgroundImg.setFitWidth(WIDTH);
        backgroundImg.setFitHeight(HEIGHT);
        character = new Character(185,700,0,0, KeyCode.LEFT,KeyCode.RIGHT,KeyCode.SPACE,KeyCode.B);

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
        Image explosionImage = new Image(Launcher.class.getResourceAsStream("explode.png"));
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
