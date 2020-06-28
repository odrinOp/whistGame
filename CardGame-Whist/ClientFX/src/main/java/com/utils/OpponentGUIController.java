package com.utils;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public class OpponentGUIController {
    Label name;
    ImageView cards_displayed;
    int no_of_cards;
    Label score;
    Label totalScore;
    int bids=0,made=0;
    int total = 0;
    private ImageView card;

    // the corresponding image will be: no_of_cards - 1;
    List<Image> back_cards_images;



    public OpponentGUIController(Label name, ImageView cards_displayed,Label score,ImageView card,Label totalScore) {
        this.name = name;
        this.cards_displayed = cards_displayed;
        this.score = score;
        this.card = card;
        this.totalScore = totalScore;
        this.card.setImage(null);
    }

    public void setImages(List<Image> images){
        this.back_cards_images = images;
    }

    public void setNumCards(int numCards){
        if(numCards < 0)
            return;
        no_of_cards = numCards;
        //setting the image for the number of cards


        if(no_of_cards >= 6)
            cards_displayed.setImage(back_cards_images.get(back_cards_images.size()-1));
        else if(no_of_cards > 0)
            cards_displayed.setImage(back_cards_images.get(no_of_cards-1));
        else
            cards_displayed.setImage(null);
    }

    public Label getName() {
        return name;
    }
    public void setBids(int bids){
        //made = 0;
        this.bids = bids;

    }

    public String getPlayerName(){
        return name.getText();
    }

    public void setMade(int made){
        this.made = made;

    }

    public void updateScore(){
        score.setText(made + "/" + bids);
        if(bids == made)
            score.setTextFill(Color.web("#fff966"));
        else
            score.setTextFill(Color.web("#ff3333"));


    }

    public void setCard(Image cardImage){
        if(cardImage == null) {
            this.card.setImage(cardImage);
            no_of_cards -=1;
            setNumCards(no_of_cards);
        }
    }


    public int getNumCards() {
        return no_of_cards;
    }

    public void updateTotal(int total) {
        totalScore.setText("Score:"+total);
    }
}
