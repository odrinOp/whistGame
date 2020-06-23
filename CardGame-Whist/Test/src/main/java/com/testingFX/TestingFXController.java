package com.testingFX;

import com.domain.Deck;
import com.testing.ImageReader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TestingFXController {

    @FXML
    private HBox playerGUI;
    @FXML
    private ImageView oppGUI2;
    @FXML
    private ImageView oppGUI3;
    @FXML
    private ImageView oppGUI4;
    private ImageReader reader;
    int nr_of_cards = 8;


    public TestingFXController() {
        try {
            reader = new ImageReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void testCards(){

        List<ImageView> images = new LinkedList<>();
        for(int i= 0; i< 8; i++){
            ImageView img = createImageView("h-14");
            images.add(img);
        }
        nr_of_cards = 8;
        updateOpponents();


        for(ImageView img: images){
            playerGUI.getChildren().add(img);
            img.setOnMouseClicked(event -> {
                playerGUI.getChildren().remove(img);
                nr_of_cards-=1;
                updateOpponents();
            });
        }








    }

    private void updateOpponents() {
        String img_id = "back-";
        if(nr_of_cards <= 0){
            oppGUI2.setImage(null);
            oppGUI3.setImage(null);
            oppGUI4.setImage(null);
            return;
        }

        if(nr_of_cards >= 6)
            img_id+="6";
        else
            img_id+=nr_of_cards;


        Image image = reader.getCardImage(img_id);

        oppGUI2.setImage(image);
        oppGUI3.setImage(image);
        oppGUI4.setImage(image);



    }

    private ImageView createImageView(String id){
        ImageView img = new ImageView();
        img.setImage(reader.getCardImage(id));
        img.setId(id);
        return img;
    }

    private void removeCardOpp(){

    }


}
