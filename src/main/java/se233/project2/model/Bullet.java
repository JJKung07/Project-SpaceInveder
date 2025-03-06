package se233.project2.model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import se233.project2.view.Platform;

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


    public boolean isEnemyBullet() {
        return isEnemyBullet;
    }
    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }
}
