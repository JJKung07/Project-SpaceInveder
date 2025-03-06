package se233.project2.model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import se233.project2.view.Platform;
import se233.project2.controller.AnimatedSprite;
import se233.project2.view.Launcher;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Pane {
    public static final int ENEMY_WIDTH = 24;
    public static final int ENEMY_HEIGHT = 24;
    private Image enemyImg;
    private int x;
    private int y;
    private AnimatedSprite imageview;
    private boolean isBulletFlight = false;
    private int xSpeed = 3;
    public boolean moveRight = true;


    public Enemy(int x,int y,int offsetX,int offsetY){
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        InputStream enemyStream = Launcher.class.getResourceAsStream("/se233/project2/enemy.png");
        if (enemyStream == null) { // Check if the resource is not found
            throw new RuntimeException("Error: Resource enemy.png not found at path /se233/project2/enemy.png");
        }
        this.enemyImg = new Image(enemyStream);

        this.imageview = new AnimatedSprite(enemyImg,1,1,1,offsetX,offsetY,ENEMY_WIDTH,ENEMY_HEIGHT);
        this.imageview.setFitWidth(ENEMY_WIDTH);
        this.imageview.setFitHeight(ENEMY_HEIGHT);
        getChildren().add(this.imageview);
    }

    public void move() {
        if (moveRight) {
            x += xSpeed;
            if (x + ENEMY_WIDTH > Platform.WIDTH) {
                moveDownAndChangeDirection();
            }
        } else {
            x -= xSpeed;
            if (x < 0) {
                moveDownAndChangeDirection();
            }
        }
        this.setTranslateX(x);
    }

    public void moveDownAndChangeDirection() {
        y += 30;
        moveRight = !moveRight;
        this.setTranslateY(y);
    }
    public void shoot() {
        javafx.application.Platform.runLater(() ->{
            if (!isBulletFlight){
                Platform platform = (Platform) getParent();
                int column = (x - 1) / 30; // Assuming each enemy is 30 units wide
                Enemy lastEnemy = platform.getLastEnemyInColumn(column);

                if (this == lastEnemy) {
                    Bullet bullet = new Bullet((int) (x + ENEMY_WIDTH / 2), (int) (y + ENEMY_HEIGHT),true);
                    platform.getChildren().add(bullet);
                    isBulletFlight = true;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isBulletFlight = false;
                        }
                    }, 500);
                }
            }
        });
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }


}
