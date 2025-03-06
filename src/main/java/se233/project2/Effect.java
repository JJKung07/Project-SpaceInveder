package se233.project2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Effect extends ImageView {
    public Effect(Image image){
        super(image);
    }
    public void playExplosionAnimation(double x, double y, Pane parentPane, Double durationInSeconds) {
        setTranslateX(x);
        setTranslateY(y);

        parentPane.getChildren().add(this);

        // Schedule a task to remove the explosion after a certain time
        javafx.animation.AnimationTimer timer = new javafx.animation.AnimationTimer() {
            long startTime = System.currentTimeMillis();

            @Override
            public void handle(long now) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > durationInSeconds * 1000) {
                    parentPane.getChildren().remove(Effect.this);
                    this.stop();
                }
            }
        };
        timer.start();
    }


}
