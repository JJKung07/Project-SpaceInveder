package se233.project2;

import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class drawingLoop implements Runnable{
    private Platform platform;
    private int frameRate;
    private float interval;
    private boolean running;
    Logger logger = LoggerFactory.getLogger(drawingLoop.class);
    public drawingLoop(Platform platform){
        this.platform = platform;
        frameRate = 30;
        interval = 1000.0f / frameRate;
        running = true;
    }

    private void checkDrawCollisions(Character character){
        character.checkReachGameWall();
    }

    private void paint(Character character){
        character.repaint();
    }

    private void moveEnemy(){
        //platform.getEnemy().move();
    }

    @Override
    public void run() {
        while (running){
            float time = System.currentTimeMillis();
            try {
                checkDrawCollisions(platform.getCharacter());
                paint(platform.getCharacter());
                moveEnemy(); // Uncomment if there are potential exceptions here
            } catch (Exception e) {
                // Handle the exception here
                logger.error("An exception occurred: " + e.getMessage());
                e.printStackTrace(); // Print the stack trace for debugging
            }
            if(time < interval){
                try {
                    Thread.sleep((long) (interval-time));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
