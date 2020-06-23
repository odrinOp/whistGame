package com.testing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class CardGUI {

    private Image image;
    private String name;
    double x,y;
    boolean selected = false;
    boolean isValid = true;


    public CardGUI(Image image, String name, double x, double y) {
        this.image = image;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void updatePosition(double x,double y){
        this.x = x;
        this.y = y;

    }

    public void render(GraphicsContext gc){
        double drawing_y = y;
        double drawing_x = x;

        if(selected)
            drawing_y -= 10;

        gc.setFill(Color.rgb(0,0,0,0.3));

        //some testing


        gc.save();
        gc.rotate(0);
        gc.drawImage(image,drawing_x,drawing_y);
        gc.restore();

        if(!isValid)
            gc.fillRect(drawing_x-0.2,drawing_y-0.2,getWidth()+0.2,getHeight()+0.2);

    }

    public double getWidth(){
        return image.getWidth();
    }

    public double getHeight(){
        return image.getHeight();
    }

    public String getName(){
        return name;
    }

    public boolean contains(double x,double y){

        if(this.x <= x && x<= this.x + getWidth() && this.y <= y && y <= this.y + getHeight())
            return true;
        return false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public boolean getSelected() {
    return selected;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardGUI cardGUI = (CardGUI) o;

        return name != null ? name.equals(cardGUI.name) : cardGUI.name == null;
    }

    @Override
    public int hashCode() {
        return getValue() * getType().hashCode();
    }

    public int getValue(){
        String[] data = name.split("-");
        return Integer.parseInt(data[1]);
    }

    public String getType(){
        String[] data = name.split("-");
        return data[0];
    }
}
