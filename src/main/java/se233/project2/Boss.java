package se233.project2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Timer;
import java.util.TimerTask;

public class Boss extends Pane {
    private static final int BOSS_WIDTH = 60;
    private static final int BOSS_HEIGHT = 37;
    private Image bossImg;
    private ImageView imageView;
    private int x;
    private int y;
    public boolean moveRight = true;
    private int xspeed = 5;
    private int health = 3;
    private boolean canShoot = true;
    private Timer shootTimer;

    public Boss(int x, int y, int offsetX, int offsetY) {
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.bossImg = new Image(Launcher.class.getResourceAsStream("Boss.png"));
        this.imageView = new AnimatedSprite(bossImg,1,1,1,offsetX,offsetY,BOSS_WIDTH,BOSS_HEIGHT);
        this.imageView.setFitWidth(BOSS_WIDTH);
        this.imageView.setFitHeight(BOSS_HEIGHT);
        getChildren().add(this.imageView);

        shootTimer = new Timer();
        shootTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                canShoot = true;
            }
        }, 300, 2000); // Adjust the delay as needed (2000 milliseconds = 2 seconds)
    }

    public void move(){
        if(moveRight){
            x += xspeed;
            if(x + BOSS_WIDTH > Platform.WIDTH){
                moveDownAndChangeDirection();
            }
        } else {
            x -= xspeed;
            if(x < 0){
                moveDownAndChangeDirection();
            }
        }
        this.setTranslateX(x);
    }

    public void moveDownAndChangeDirection() {
        y += 50;
        moveRight = !moveRight;
        this.setTranslateY(y);
    }
    public void hit() {
        health--;
        if (health <= 0) {
            Platform platform = (Platform) getParent();
            platform.getChildren().remove(this);
            platform.setHasBoss(false); // Set hasBoss to false
            // Respawn enemies


            System.out.println("Boss Defeated, hasBoss set to false");
        }
    }
    public boolean isDefeated() {
        return health <= 0;
    }

    public void shootSpecial(){
        if (canShoot) {
            // Shoot downward
            Bullet bulletDown = new Bullet(x + BOSS_WIDTH / 2, y + BOSS_HEIGHT, true);

            // Shoot diagonal bottom left
            Bullet bulletLeft = new Bullet(x, y + BOSS_HEIGHT, true);
            bulletLeft.setXSpeed(-10); // Set the desired xSpeed

            // Shoot diagonal bottom right
            Bullet bulletRight = new Bullet(x + BOSS_WIDTH, y + BOSS_HEIGHT, true);
            bulletRight.setXSpeed(+10); // Set the desired xSpeed

            Platform platform = (Platform) getParent();
            platform.getChildren().addAll(bulletDown, bulletLeft, bulletRight);

            canShoot = false; // Prevent shooting for the specified delay
        }
    }
    public void shootNormal(){
        if (canShoot) {
            // Shoot downward
            Bullet bulletDown = new Bullet(x + BOSS_WIDTH / 2, y + BOSS_HEIGHT + 3, true);

            // Shoot diagonal bottom left
            Bullet bulletLeft = new Bullet(x, y + BOSS_HEIGHT, true);

            // Shoot diagonal bottom right
            Bullet bulletRight = new Bullet(x + BOSS_WIDTH, y + BOSS_HEIGHT + 5, true);

            Platform platform = (Platform) getParent();
            platform.getChildren().addAll(bulletDown, bulletLeft, bulletRight);

            canShoot = false; // Prevent shooting for the specified delay
        }
    }

    public void Shoot(){
        if (health > 0 && canShoot) { // Add a check for boss health
            double random = Math.random();
            if (random < 0.5) {
                shootSpecial();
            } else {
                shootNormal();
            }

            canShoot = false; // Prevent shooting for the specified delay
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

