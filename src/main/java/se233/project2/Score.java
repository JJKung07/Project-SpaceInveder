package se233.project2;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Score extends Label {
    private int score = 0;
    Logger logger = LoggerFactory.getLogger(Score.class);

    public Score(){
        this.setText("Score: " + score);
        this.setTextFill(Color.WHITE);
        this.setFont(Font.font("Times New Roman",30));
    }
    public void increaseScore(int point){
        score += point;
        this.setText("Score: " + score);
        logger.info("Increased score by " + point + ". New score: "+ score);
    }
}
