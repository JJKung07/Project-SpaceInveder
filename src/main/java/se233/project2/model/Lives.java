package se233.project2.model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import se233.project2.controller.AnimatedSprite;

public class Lives extends Pane {
    public static final int LIVES_WIDTH = 24;
    public static final int LIVES_HEIGHT = 24;
    private Image liveImg;
    private int x;
    private int y;
    private int startX;
    private int startY;
    private AnimatedSprite imageView;

    public Lives(int X,int Y,int offsetX,int offsetY){
        this.x = X;
        this.y = Y;
        this.startX = X;
        this.startY = Y;
        this.setTranslateX(X);
        this.setTranslateY(Y);
        // Correct the resource path to include the full location
        this.liveImg = new Image(getClass().getResourceAsStream("/se233/project2/spaceship.png"));
        if (this.liveImg == null) {
            throw new RuntimeException("Failed to load spaceship.png");
        }

        this.imageView = new AnimatedSprite(liveImg,1,1,1,offsetX,offsetY,LIVES_WIDTH,LIVES_HEIGHT);
        this.imageView.setFitWidth(LIVES_WIDTH);
        this.imageView.setFitHeight(LIVES_HEIGHT);
        getChildren().add(this.imageView);
    }
}
