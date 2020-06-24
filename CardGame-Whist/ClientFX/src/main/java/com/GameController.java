package com;

import com.domain.Player;
import com.utils.ImageReader;
import com.utils.OpponentGUIController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.LinkedList;
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
            opName3,
            opScore1,
            opScore2,
            opScore3;



    @FXML private HBox playerGUI;
    @FXML private Label playerScore;

    @FXML private Label bidsLabel;
    @FXML private TextField bidsTxt;
    @FXML private Button submitBid;

    private OpponentGUIController opp1;
    private OpponentGUIController opp2;
    private OpponentGUIController opp3;

    private boolean isYourTurn = false;
    private List<OpponentGUIController> oppControllers;

    private int bid=0,made=0;

    //private GraphicsContext gc;

    public GameController() {
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void initState(){
        hideBidGUI();
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
    );


        return img;
    }



    public void initPlayersPositions(List<Player> players, List<Image> back_images_opp) {
        if(players.size() != 4)
            return;

        opp1 = createOppGUI(back_images_opp,opName1,opGUI1,opScore1);
        opp2 = createOppGUI(back_images_opp,opName2,opGUI2,opScore2);
        opp3 = createOppGUI(back_images_opp,opName3,opGUI3,opScore3);

        //setting the name
        opp1.getName().setText(players.get(1).getNickname());
        opp2.getName().setText(players.get(2).getNickname());
        opp3.getName().setText(players.get(3).getNickname());

        oppControllers = new LinkedList<>();
        oppControllers.add(opp1);
        oppControllers.add(opp2);
        oppControllers.add(opp3);


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

    private OpponentGUIController createOppGUI(List<Image> images, Label name, ImageView view,Label score){
        OpponentGUIController op =  new OpponentGUIController(name,view,score);
        op.setImages(images);
        return op;
    }

    public void hideBidGUI(){
        bidsLabel.setDisable(true);
        bidsLabel.setVisible(false);

        bidsTxt.setDisable(true);
        bidsTxt.setVisible(false);

        submitBid.setDisable(true);
        submitBid.setVisible(false);
    }

    public void showBidGui(){
        bidsLabel.setDisable(false);
        bidsLabel.setVisible(true);

        bidsTxt.setDisable(false);
        bidsTxt.setVisible(true);

        submitBid.setDisable(false);
        submitBid.setVisible(true);
    }

    @FXML
    private void submitBidResponse(){

        bid = Integer.parseInt(bidsTxt.getText());
        mainController.sendBid(bid);


    }

    public void setBidStatus(int bid) {
        playerScore.setText(made + "/" + bid);
        if(made == bid)
            playerScore.setTextFill(Color.web("#fff966"));
        else
            playerScore.setTextFill(Color.web("#ff3333"));
    }

    public void setBids(Map<String, Integer> bidsByPlayers) {
        for(OpponentGUIController opponent: oppControllers){
            String name = opponent.getPlayerName();
            if(!bidsByPlayers.containsKey(name))
                continue;

            int bid = bidsByPlayers.get(name);
            opponent.setBids(bid);
        }
    }
}
