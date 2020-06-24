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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.utils.ImageReader;

import java.io.IOException;
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


    public void login(String nickname) throws AppException, IOException {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            server = (IServer) factory.getBean("appServer");
            this.player = server.login(nickname,this);
            System.out.println("Name: "+ this.player.getNickname());
            this.lobbyController.initState();
            this.sceneManager.changeActiveScene(ApplicationState.LOBBY);
            reader = new ImageReader();
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
        //server.sendCard(id);
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
        gameController.setBidStatus(bid);
    }
}
