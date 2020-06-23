package com.testing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class OpponentGUIController {

    private String position ; //left,right,top
    private int numberOfCards;
    private Image back_card_image;
    double x,y;
    double width=10,height=10;
    double positionCenterX,positionCenterY;

    public OpponentGUIController(String position, Image back_card_image,int numberOfCards,double positionCenterX,double positionCenterY) {
        this.position = position;
        this.back_card_image = back_card_image;
        this.numberOfCards = numberOfCards;
        this.positionCenterX = positionCenterX;
        this.positionCenterY = positionCenterY;
        calculateCoord();
    }

    boolean isCentered(){
        double centerX = (2*x+width)/2;
        double centerY = (2*y+height)/2;

        if(centerX != positionCenterX )
            return false;

        if(centerY != positionCenterY)
            return false;
        return true;
    }

    private void calculateCoord() {
        //calculate the width;
        if(position.equals("top")) {
            height = 20 + back_card_image.getHeight();
            width = 20 + back_card_image.getWidth() * numberOfCards - 15 * numberOfCards;
        }
        else{
            height = 20 + back_card_image.getWidth() * numberOfCards -15*numberOfCards;
            width = 20 + back_card_image.getHeight();
        }
        centerGUI();
    }

    private void centerGUI() {
        while(!isCentered()){
            double centerX = (2*x+width)/2;
            double centerY = (2*y+height)/2;

            if(centerX < positionCenterX)
                x+=0.5;
            else if(centerX>positionCenterX)
                x-=0.5;

            if(centerY < positionCenterY)
                y+=0.5;
            if(centerY>positionCenterY)
                y-=0.5;
        }
    }


    public void render(GraphicsContext gc){
        gc.fillRect(x,y,width,height);

        for (int i = 0; i<numberOfCards;i++) {
            gc.save();
            gc.rotate(0);
            gc.drawImage(back_card_image, 50+x, 5 + y + back_card_image.getWidth() * i + -16 * i);
            System.out.println("y: " + (5 + y + back_card_image.getWidth() * i + -16 * i ));
            System.out.println("x: " + (10+x));
            gc.restore();
        }
    }
}
