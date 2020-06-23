package com.testing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerGUIController {

    private List<CardGUI> cards;
    private double x,y;
    private double width,height;
    private Color color;
    private double positionCenterX;
    private double positionCenterY;
    private CardGUI selectedCard = null;

    public PlayerGUIController(Color color,double positionCenterX,double positionCenterY) {
        this.x = 0;
        this.y = 0;
        this.width = 20;
        this.height = 20;
        this.color = color;
        this.cards = new LinkedList<>();
        this.positionCenterX = positionCenterX;
        this.positionCenterY = positionCenterY;
    }

    private void orderCards(){
        cards = cards.stream().sorted((x,y)->{
            if(x.getType().compareTo(y.getType()) == 0)
                return x.getValue() - y.getValue();
            return x.getType().compareTo(y.getType());
        }).collect(Collectors.toList());
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

    void centerGUI(){
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

    public void addCard(String name,Image img){

        CardGUI c = new CardGUI(img,name,x+10 + img.getWidth() * (cards.size()+1), y+10);
        if(cards.size() % 2 ==0)
            c.setValid(true);
        cards.add(c);
        orderCards();
        width = 20 + img.getWidth() * cards.size();
        height = 20 +img.getHeight();
        centerGUI();
        updateCardsPositions();

    }

    private void updateCardsPositions(){
        int i= 0;
        for(CardGUI cardGUI: cards){
            cardGUI.updatePosition(x+10+cardGUI.getWidth() *i,y+10);
            i+=1;
        }
    }

    void render(GraphicsContext gc){
        gc.setFill(Color.RED);
        gc.fillRect(x,y,width,height);


        //gc.setFill(color);
        //gc.fillRect(x,y,width,height);

        renderCards(gc);
    }

    private void renderCards(GraphicsContext gc) {
        int i = 0;
        for(CardGUI c: cards)
            c.render(gc);
    }

    public void mouseEvent(MouseEvent event){
        double x = event.getX();
        double y = event.getY();

        CardGUI clickedCard = contains(x,y);
        System.out.println(clickedCard);
        if(clickedCard == null)
            return; // no card has been selected

        if(selectedCard != null && clickedCard == selectedCard)
        {
            if (clickedCard.isValid()) {
                removeCard(clickedCard);
                selectedCard = null;
            }
            return;
        }

        if(selectedCard != null && clickedCard != selectedCard){
            setSelectedOnAll(false);
            clickedCard.setSelected(true);
            updateStatus(clickedCard);
            selectedCard = clickedCard;
            return;
        }

        if(selectedCard == null){
            clickedCard.setSelected(true);
            updateStatus(clickedCard);
            selectedCard = clickedCard;
            return;
        }

    }

    private void setSelectedOnAll(boolean selected){
        for (CardGUI cardGUI: cards)
            cardGUI.setSelected(selected);
    }

    private void updateStatus(CardGUI card){
        for(CardGUI cardGUI: cards)
            if(cardGUI.getName().equals(card.getName())){
                cardGUI.setSelected(card.getSelected());
                cardGUI.setValid(card.isValid());
                return;
            }
    }

    private CardGUI contains(double x, double y) {
        for(CardGUI cardGUI: cards)
            if(cardGUI.contains(x,y))
                return cardGUI;
            return null;
    }

    private void removeCard(CardGUI c) {
        cards.remove(c);
        width = 20 + c.getWidth() * cards.size();
        centerGUI();
        updateCardsPositions();
    }

    public void showCardsName(){
        for (CardGUI c: cards){
            System.out.println("Card: " + c.getName() + "\n x:" + c.getX() + " y:" + c.getY());
        }
    }

    private void swapCards(){

    }
}
