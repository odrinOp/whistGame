package com;

import com.domain.Player;
import com.utils.OpponentGUIController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.HashMap;
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
            opScore3,
            opTotal1,
            opTotal2,
            opTotal3;

    @FXML
    private ImageView opCard1,
            opCard2,
            opCard3,
            playerCard,
            atuCard;



    @FXML private HBox playerGUI;
    @FXML private Label playerScore;
    @FXML private Label playerTotal;

    @FXML private Label bidsLabel;
    @FXML private TextField bidsTxt;
    @FXML private Button submitBid;

    private OpponentGUIController opp1;
    private OpponentGUIController opp2;
    private OpponentGUIController opp3;

    private boolean isYourTurn = false;
    private List<OpponentGUIController> oppControllers;

    private int bid=0,made=0;
    private int score = 0;
    //private GraphicsContext gc;

    public GameController() {
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void initState(){
        bid = made = score = 0;
        hideBidGUI();
        playerCard.setImage(null);
        atuCard.setImage(null);
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

            isYourTurn = false;
        }
    );


        return img;
    }



    public void initPlayersPositions(List<Player> players, List<Image> back_images_opp) {
        if(players.size() != 4)
            return;

        opp1 = createOppGUI(back_images_opp,opName1,opGUI1,opScore1,opCard1,opTotal1);
        opp2 = createOppGUI(back_images_opp,opName2,opGUI2,opScore2,opCard2,opTotal2);
        opp3 = createOppGUI(back_images_opp,opName3,opGUI3,opScore3,opCard3,opTotal3);

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
            view.setOpacity(0.4);
            view.setDisable(true);
            playerGUI.getChildren().add(view);
        }

        int number_of_cards = cards.size();

        opp1.setNumCards(number_of_cards);
        opp2.setNumCards(number_of_cards);
        opp3.setNumCards(number_of_cards);
    }

    private OpponentGUIController createOppGUI(List<Image> images, Label name, ImageView view, Label score, ImageView card,Label total){
        OpponentGUIController op =  new OpponentGUIController(name,view,score,card,total);
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
        if(bid <= getNoCards())
            mainController.sendBid(bid);


    }

    private int getNoCards() {
        return playerGUI.getChildren().size();
    }

    public void setBidStatus() {
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
            opponent.updateScore();
        }
    }

    public void updateCardsGUI(String firstCard, String atuCard) {
        //daca firstCard == null; atunci putem pune orice carte
        //daca firstCard!=null; atunci putem pune doar cartile de tipul respectiv, sau doar cartile de atu; si daca nu avem, atunci putem pune orice;

        isYourTurn = true;
        if(firstCard == null) {
            makeCardsAvailable(null);
            return;
        }

        int firstCardType = getCardsType(firstCard);
        if(firstCardType != 0){
            makeCardsAvailable(firstCard);
            return;
        }
        if(atuCard == null){
            makeCardsAvailable(null);
            return;
        }
        int atuCardType = getCardsType(atuCard);
        if(atuCardType != 0){
            makeCardsAvailable(atuCard);
            return;
        }

        makeCardsAvailable(null);


    }

    private void makeCardsAvailable(String firstCard) {
        String type = null;

        if (firstCard != null)
            type = firstCard.split("-")[0];
        for (Node node : playerGUI.getChildren()) {
            if (node instanceof ImageView) {
                ImageView card = (ImageView) node;
                String cardType = card.getId().split("-")[0];
                if(type == null)
                {
                    card.setOpacity(1);
                    card.setDisable(false);
                    continue;
                }
                if(type.equals(cardType)){
                    card.setOpacity(1);
                    card.setDisable(false);
                }
                else{
                    card.setOpacity(0.4);
                    card.setDisable(true);
                }

            }
        }
    }

    private int getCardsType(String firstCard) {
        System.out.println(firstCard);
        String cardType = firstCard.split("-")[0];
        int count = 0;
        for (Node node: playerGUI.getChildren()){
            if(node instanceof ImageView){
                ImageView card = (ImageView) node;

                String type = card.getId().split("-")[0];
                if(type.equals(cardType))
                    count += 1;
            }
        }
        return count;
    }

    public void setPlayerCard(Image player_cardImage) {
        playerCard.setImage(player_cardImage);
    }

    public void setOpponentsCards(HashMap<String, Image> opp_cardImage) {
        for(OpponentGUIController oppCtrl: oppControllers){
            if(opp_cardImage.get(oppCtrl.getPlayerName()) != null){
                Image cardImage = opp_cardImage.get(oppCtrl.getPlayerName());
                oppCtrl.setCard(cardImage);
            }
            else
            {
                oppCtrl.setCard(null);
            }
        }
    }

    public void setScoreForPlayer(Pair<Integer, Integer> playerScore) {
        made = playerScore.getValue();
        bid = playerScore.getKey();
        setBidStatus();
    }

    public void setScoreForOpponents(Map<String, Pair<Integer, Integer>> opponentScore) {
        for(OpponentGUIController opp: oppControllers){
            Pair<Integer,Integer> score = opponentScore.get(opp.getPlayerName());
            if(score == null)
                continue;

            opp.setMade(score.getValue());
            opp.setBids(score.getKey());
            opp.updateScore();
        }
    }

    public void clearTable() {
        playerCard.setImage(null);
        for(OpponentGUIController opp: oppControllers){
            opp.setCard(null);
            opp.setNumCards(opp.getNumCards() - 1);
        }
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void disableCards() {
        for(Node node: playerGUI.getChildren()){
            if(node instanceof ImageView){
                ImageView cardView = (ImageView) node;
                cardView.setDisable(true);
                cardView.setOpacity(0.4);
            }
        }
    }

    public void resetPlayerScore() {
        bid = 0;
        made = 0;
        setBidStatus();
    }

    public void resetOpponentScore() {
        for(OpponentGUIController opp: oppControllers){
            opp.setMade(0);
            opp.setBids(0);
            opp.updateScore();
        }
    }

    public void updateAtuImage(Image atuImage) {
        atuCard.setImage(atuImage);
    }

    public void updatePlayerTotal(int playerTotal) {
        this.playerTotal.setText("Score:"+playerTotal);
    }

    public void updateOppTotal(Map<String, Integer> oppTotal) {
        for(OpponentGUIController ctrl: oppControllers){
            int total = oppTotal.get(ctrl.getName());
            ctrl.updateTotal(total);
        }
    }
}
