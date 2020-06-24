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
    int bids=0,made=0;

    // the corresponding image will be: no_of_cards - 1;
    List<Image> back_cards_images;



    public OpponentGUIController(Label name, ImageView cards_displayed,Label score) {
        this.name = name;
        this.cards_displayed = cards_displayed;
        this.score = score;
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
        made = 0;
        this.bids = bids;

        score.setText(made + "/"+bids);
        if(bids == made)
            score.setTextFill(Color.web("#fff966"));
        else
            score.setTextFill(Color.web("#ff3333"));
    }

    public String getPlayerName(){
        return name.getText();
    }

    public void incrementMade(){
        made += 1;
        score.setText(made + "/" + bids);
        if(bids == made)
            score.setTextFill(Color.web("#fff966"));
        else
            score.setTextFill(Color.web("#ff3333"));
    }


}
