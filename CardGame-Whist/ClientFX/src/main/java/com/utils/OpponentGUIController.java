package com.utils;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class OpponentGUIController {
    Label name;
    ImageView cards_displayed;
    int no_of_cards;

    // the corresponding image will be: no_of_cards - 1;
    List<Image> back_cards_images;

    int bids;
    int score_per_round;


    public OpponentGUIController(Label name, ImageView cards_displayed) {
        this.name = name;
        this.cards_displayed = cards_displayed;
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
}
