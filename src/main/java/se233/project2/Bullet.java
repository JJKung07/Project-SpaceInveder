package se233.project2;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Bullet extends Line {

    private int ySpeed = -5;
    private int xSpeed;
    private int x;
    private int y;
    private boolean isEnemyBullet;

    public Bullet(int x, int y, boolean isEnemyBullet) {
        this.x = x;
        this.y = y;
        this.isEnemyBullet = isEnemyBullet;

        this.setStartX(x);
        this.setStartY(y);
        this.setEndX(x);
        this.setEndY(y + (isEnemyBullet ? 10 : -10));

        this.setStrokeWidth(2);
        this.setStroke(isEnemyBullet ? Color.WHITE : Color.RED);

        this.ySpeed = isEnemyBullet ? 10 : -15; // Adjust speed as needed
    }

    public void update() {
        setStartY(getStartY() + ySpeed);
        setEndY(getEndY() + ySpeed);
        setStartX(getStartX() + xSpeed);
        setEndX(getEndX() + xSpeed);

        javafx.application.Platform.runLater(() -> {
            Node parent = getParent();
            if (parent instanceof Platform platform) {
                // Check if the bullet is out of bounds
                if (getStartY() < 0 || getStartY() > Platform.HEIGHT ||
                        getStartX() < 0 || getStartX() > Platform.WIDTH) {
                    platform.getChildren().remove(this);
                }

                // Reflect the bullet if it hits the left or right border
                if (getStartX() < 0 || getStartX() > Platform.WIDTH) {
                    xSpeed = -xSpeed;
                }
            }
        });
    }

    public void checkCollisionWithCharacter(Character character) {
        javafx.application.Platform.runLater(() -> {
            if (character.getBoundsInParent().intersects(this.getBoundsInParent())) {
                Node parent = getParent();
                if (parent instanceof Platform platform) {
                    platform.getChildren().remove(this);

                    if (isEnemyBullet) {
                        character.setLives(character.getLives() - 1);
                        if (character.getLives() <= 0) {
                            platform.getChildren().remove(character);
                        }
                    } else {
                        List<Node> toRemove = new ArrayList<>();
                        platform.getEnemy().forEach(enemy -> {
                            if (enemy.getBoundsInParent().intersects(this.getBoundsInParent())) {
                                toRemove.add(enemy);
                            }
                        });
                        platform.getChildren().removeAll(toRemove);

                        Boss boss = platform.getBoss();
                        if (boss != null && boss.getBoundsInParent().intersects(this.getBoundsInParent())) {
                            boss.hit();
                            if (boss.isDefeated()) {
                                platform.getChildren().remove(boss);

                            }
                        }
                    }
                }
            }
        });
    }


    public boolean isEnemyBullet() {
        return isEnemyBullet;
    }
    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }
}
