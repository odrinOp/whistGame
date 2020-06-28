package com;

import com.domain.Card;
import com.domain.Player;
import com.dto.GameData;
import com.dto.LobbyData;
import com.dto.StartGameData;
import com.utils.ApplicationState;
import com.utils.SceneManager;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.utils.ImageReader;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainController extends UnicastRemoteObject implements IClientObserver {
    private IServer server;
    private Player player;
    private SceneManager sceneManager;

    private LoginController loginController;
    private LobbyController lobbyController;
    private GameController gameController;
    private ImageReader reader;

    private boolean bidRequest = false;
    private int bidUnavailable = -1;

    private boolean cardRequest = false;



    public MainController() throws RemoteException {
    }

    public MainController(IServer server, Player player, Stage stage) throws RemoteException {
        super();
        this.server = server;
        this.player = player;
        this.sceneManager = new SceneManager(stage);
    }

    @Override
    public void update(GameData data) throws RemoteException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(data instanceof LobbyData){
                    try {
                        lobbyController.update(data);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                if(data instanceof StartGameData){
                    try {
                        gameController.initState();
                        sceneManager.changeActiveScene(ApplicationState.GAME);
                        StartGameData startGameData = (StartGameData) data;
                        Player[] players = startGameData.getPlayers();

                        List<Image> back_images_opp = getBackImagesOpponent();

                        gameController.initPlayersPositions(rotatePlayerList(players),back_images_opp);
                        System.out.println(players[0].getNickname() + " will start the game");


                    } catch (AppException appException) {
                        appException.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void receiveCards(List<Card> value) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Map<String , Image> cards = new HashMap<>();
                for(Card c: value){
                    Image img = reader.getCardImage(c.getName());
                    cards.put(c.getName(),img);
                }

                gameController.initPlayersCards(cards);
            }
        });

    }

    @Override
    public void kick(String s) {
        System.out.println(s);
        System.exit(0);
    }

    @Override
    public void getBidRequest(int unavailableBid) throws RemoteException {

        if(bidRequest == false){
            bidRequest = true;
            gameController.showBidGui();
            this.bidUnavailable = unavailableBid;
        }

    }

    @Override
    public void updateBidsInfo(Map<String, Integer> bidsByPlayers) throws RemoteException {
        Platform.runLater(()->{
            gameController.setBids(bidsByPlayers);
        });
    }

    @Override
    public void requestCard(String firstCard, String atuCard) throws RemoteException {
        if (cardRequest == false) {
            cardRequest = true;
            gameController.updateCardsGUI(firstCard, atuCard);
        }
    }

    @Override
    public void sendCardsOnTable(List<Pair<String, Card>> players_cards) throws RemoteException {
        Image player_cardImage = null;
        HashMap<String,Image> opp_cardImage = new HashMap<>();
        for(Pair<String,Card> player_cards : players_cards){
            String name = player_cards.getKey();
            String cardName = player_cards.getValue().getName();

            if(name.equals(player.getNickname())){
                player_cardImage = reader.getCardImage(cardName);
            }
            else{
                Image oppCardImage = reader.getCardImage(cardName);
                opp_cardImage.put(name,oppCardImage);
            }
        }

        gameController.setPlayerCard(player_cardImage);
        gameController.setOpponentsCards(opp_cardImage);
        gameController.disableCards();
    }

    @Override
    public void sendPlayersBalance(Map<String, Pair<Integer, Integer>> playersScore) throws RemoteException {
        Platform.runLater(()->
        {
            Pair<Integer,Integer> playerScore = new Pair<>(0,0);
            Map<String,Pair<Integer,Integer>> opponentScore = new HashMap<>();

            for(Map.Entry<String,Pair<Integer,Integer>> entry: playersScore.entrySet()){
                if(entry.getKey().equals(player.getNickname()))
                    playerScore = entry.getValue();
                else{
                    opponentScore.put(entry.getKey(),entry.getValue());
                }
            }

            gameController.setScoreForPlayer(playerScore);
            gameController.setScoreForOpponents(opponentScore);
            gameController.clearTable();
        });


    }

    @Override
    public void resetRound() throws RemoteException {
        Platform.runLater(()->{
            gameController.resetPlayerScore();
            gameController.resetOpponentScore();
            gameController.updateAtuImage(null);
        });

    }

    @Override
    public void updateAtu(String atuString) throws RemoteException {
        Platform.runLater(()->{
            System.out.println(atuString);
            Image atuImage = reader.getCardImage(atuString);
            gameController.updateAtuImage(atuImage);
        });
    }

    @Override
    public void updateTotalScore(Map<String, Integer> playersScore) throws RemoteException {
        Platform.runLater(()->{
            Map<String,Integer> oppTotal = new HashMap<>();
            int playerTotal = 0;

            for(Map.Entry<String,Integer> total: playersScore.entrySet()){
                if(total.getKey().equals(player.getNickname()))
                    playerTotal = total.getValue();
                else
                    oppTotal.put(total.getKey(),total.getValue());
            }


            gameController.updatePlayerTotal(playerTotal);
            gameController.updateOppTotal(oppTotal);
        });



    }


    public void login(String nickname,String ipAddr,String port) throws AppException, IOException {

            configureXMLFile(ipAddr,port);
            System.out.println(InetAddress.getByName("localhost"));
            ApplicationContext factory = null;
            try {
                factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
                System.out.println(InetAddress.getByName("localhost"));
            }catch (BeanCreationException e){
                throw new AppException("There is no server on this IP Address or port! Try again!");
            }
            server = (IServer) factory.getBean("appServer");
            this.player = server.login(nickname,this);
            System.out.println("Name: "+ this.player.getNickname());
            this.lobbyController.initState();
            this.sceneManager.changeActiveScene(ApplicationState.LOBBY);
            reader = new ImageReader();
    }

    private void configureXMLFile(String ipAddr, String port) {

        String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd\">\n" +
                "\n" +
                "    <bean id=\"appServer\" class=\"org.springframework.remoting.rmi.RmiProxyFactoryBean\"><property name=\"serviceUrl\" value=\"rmi://" + ipAddr + ":" + port + "/WhistGame\"/><property name=\"serviceInterface\" value=\"com.IServer\"/>\n" +
                "    </bean>\n" +
                "\n" +
                "</beans>";

        try {
            FileWriter writer = new FileWriter("C:\\GitProjects\\whistGame\\CardGame-Whist\\ClientFX\\src\\main\\resources\\spring-client.xml");
            writer.write(xmlFile);
            System.out.println("Configuration Done!");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void markAsReady() throws AppException {
        server.markAsActive(player);
        player.setReady(true);
    }


    public IServer getServer() {
        return server;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void startGame() {
        try {
            this.server.startGame();

        } catch (AppException appException) {
            appException.printStackTrace();
        }

    }


    private List<Player> rotatePlayerList(Player[] players){
        List<Player> rotatedPlayers = new LinkedList<>();
        //get the index of player:
        int index = -1;
        for(int i =0;i < players.length; i++){
            if(players[i].getNickname().equals(this.player.getNickname())) {
                index = i;
                break;
            }
        }

        rotatedPlayers.add(players[index]);
        index +=1;
        if(index == players.length)
            index = 0;
        //copying elements to the new list
        while(!players[index].getNickname().equals(this.player.getNickname())){
            rotatedPlayers.add(players[index]);
            index+=1;
            if(index == players.length)
                index = 0;
        }

        return rotatedPlayers;

    }


    private List<Image> getBackImagesOpponent(){
        List<Image> images = new LinkedList<>();
        String img_name = "back-";
        for(int i=1;i<=6;i++){
            images.add(reader.getCardImage(img_name + i));
        }

        return images;
    }

    public void sendCard(String id) {
        server.sendCard(player.getNickname(),id);
        cardRequest = false;
    }

    public void sendBid(int bid) {
        if(bidUnavailable == bid)
        {
            System.out.println("You can't bid " + bid + "!");
            return;
        }
        server.sendBid(bid);
        bidRequest = false;
        gameController.hideBidGUI();
        gameController.setBid(bid);

        gameController.setBidStatus();
    }

}
