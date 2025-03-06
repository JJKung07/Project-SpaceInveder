package se233.project2;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Character extends Pane {
    public static final int CHARACTER_WIDTH = 24;
    public static final int CHARACTER_HEIGHT = 24;
    private Image characterImg;
    private int x;
    private int y;
    private int startX;
    private int startY;
    private KeyCode leftkey;
    private KeyCode rightkey;
    private KeyCode shootekey;
    private KeyCode Bshoot;
    private boolean isMoveLeft;
    private boolean isMoveRight;
    private AnimatedSprite imageView;
    private boolean bulletFlight = false;
    private int live = 3;
    Logger logger = LoggerFactory.getLogger(Character.class);
    private int specialShootCount = 0;
    private int MAX_SPECIAL_SHOOTS = 4;

    public Character (int x, int y,int offsetX,int offsetY, KeyCode LEFT,KeyCode RIGHT,KeyCode SPACE,KeyCode B){
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.leftkey = LEFT;
        this.rightkey = RIGHT;
        this.shootekey = SPACE;
        this.Bshoot = B;
        this.characterImg = new Image(Launcher.class.getResourceAsStream("spaceship.png"));
        this.imageView = new AnimatedSprite(characterImg,1,1,1,offsetX,offsetY,24,24);
        this.imageView.setFitWidth(CHARACTER_WIDTH);
        this.imageView.setFitHeight(CHARACTER_HEIGHT);

        getChildren().add(this.imageView);

    }

    public void checkReachGameWall(){
        if(x <= 0){
            x=0;
        } else if (x+getWidth() >= Platform.WIDTH) {
            x = Platform.WIDTH - (int)getWidth();
        }
    }

    public void moveLeft(){
        isMoveLeft = true;
        isMoveRight = false;
        updatePosition();
    }

    public void moveRight(){
        isMoveLeft = false;
        isMoveRight = true;
        updatePosition();
    }

    public void moveX(){
        setTranslateX(x);
    }

    public void updatePosition(){
        if(isMoveLeft){
            x -= 10;
        } else if (isMoveRight) {
            x += 10;
        }
        setTranslateX(x);
        checkReachGameWall();
    }
    public void stopMoving(){
        isMoveLeft = false;
        isMoveRight = false;
    }
    public void shoot(){
        javafx.application.Platform.runLater(() ->{
            if(!bulletFlight){
                Bullet bullet = new Bullet((int) (x + CHARACTER_WIDTH / 2), (int) (y - 10),false);
                Platform platform = (Platform) getParent();
                platform.getChildren().add(bullet);
                bulletFlight = true;

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bulletFlight = false;
                    }
                },500);
            }
        });
    }
    public void shootSpecial(){
        javafx.application.Platform.runLater(() ->{
            if(!bulletFlight && specialShootCount < MAX_SPECIAL_SHOOTS){ // Check if special shoot count is less than maximum
                specialShootCount++; // Increment the special shoot count
                Bullet bulletDown = new Bullet(x + CHARACTER_WIDTH / 2, y - CHARACTER_HEIGHT + 3, false);

                // Shoot diagonal bottom left
                Bullet bulletLeft = new Bullet(x, y - CHARACTER_HEIGHT, false);

                // Shoot diagonal bottom right
                Bullet bulletRight = new Bullet(x + CHARACTER_WIDTH, y - CHARACTER_HEIGHT + 5, false);

                Platform platform = (Platform) getParent();
                platform.getChildren().addAll(bulletDown, bulletLeft, bulletRight);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bulletFlight = false;
                    }
                },1000);
            }
        });
    }

    public KeyCode getBshoot() {
        return Bshoot;
    }

    public void repaint(){
        moveX();
    }

    public KeyCode getLeftkey() {
        return leftkey;
    }

    public KeyCode getRightkey() {
        return rightkey;
    }

    public KeyCode getShootekey() {
        return shootekey;
    }

    public AnimatedSprite getImageView() {
        return imageView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLives() {
        return live;
    }

    public void setLives(int live) {
        this.live = live;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void trace(){
        logger.info("x:{} y:{}",x,y);
    }


}
