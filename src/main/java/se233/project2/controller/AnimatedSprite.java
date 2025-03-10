package se233.project2.controller;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimatedSprite extends ImageView {
    int count, columns, rows, offsetX, offsetY, width, height, curindex, curColumnIndex = 0, curRowIndex = 0;
    public AnimatedSprite(Image image, int count, int columns, int rows, int offsetX,
                          int offsetY, int width, int height){
        this.setImage(image);
        this.count =count;
        this.columns = columns;
        this.rows = rows;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.setViewport(new Rectangle2D(offsetX,offsetY,width,height));
    }
    public void tick(){
        curColumnIndex = curindex % columns;
        curRowIndex = curindex / columns;
        curindex = (curindex+1) % (columns * rows);
        interpolate();
    }
    protected void interpolate(){
        final int x = curColumnIndex * width + offsetX;
        final int y = curRowIndex * height + offsetY;
        this.setViewport(new Rectangle2D(x,y,width,height));
    }
}
