package com;

import com.domain.Player;
import com.utils.ImageReader;
import com.utils.OpponentGUIController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public class GameController {
    private MainController mainController;

    @FXML
    private ImageView opGUI1,
            opGUI2,
            opGUI3;
    @FXML
    private Label opName1,
            opName2,
            opName3;


    @FXML private HBox playerGUI;

    private OpponentGUIController opp1;
    private OpponentGUIController opp2;
    private OpponentGUIController opp3;

    private boolean isYourTurn = false;

    private GraphicsContext gc;

    public GameController() {
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void initState(){

    }



    private ImageView createCardView(Map.Entry<String, Image> card) {
        ImageView img = new ImageView();
        img.setImage(card.getValue());
        img.setId(card.getKey());
        img.setOnMouseClicked(event ->{
            if(!isYourTurn) return;

            playerGUI.getChildren().remove(img);
            String id = img.getId();
            mainController.sendCard(id);
        }
));


        return img;
    }



    public void initPlayersPositions(List<Player> players, List<Image> back_images_opp) {
        if(players.size() != 4)
            return;

        opp1 = createOppGUI(back_images_opp,opName1,opGUI1);
        opp2 = createOppGUI(back_images_opp,opName2,opGUI2);
        opp3 = createOppGUI(back_images_opp,opName3,opGUI3);

        //setting the name
        opp1.getName().setText(players.get(1).getNickname());
        opp2.getName().setText(players.get(2).getNickname());
        opp3.getName().setText(players.get(3).getNickname());

    }

    public void initPlayersCards(Map<String, Image> cards) {

        for(Map.Entry<String,Image> card: cards.entrySet()){
            ImageView view = createCardView(card);
            playerGUI.getChildren().add(view);
        }

        int number_of_cards = cards.size();

        opp1.setNumCards(number_of_cards);
        opp2.setNumCards(number_of_cards);
        opp3.setNumCards(number_of_cards);
    }

    private OpponentGUIController createOppGUI(List<Image> images, Label name, ImageView view){
        OpponentGUIController op =  new OpponentGUIController(name,view);
        op.setImages(images);
        return op;
    }
}
