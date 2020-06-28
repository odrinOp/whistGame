package com;


import com.domain.Card;
import com.domain.Player;
import com.dto.GameData;
import com.dto.LobbyData;
import com.dto.StartGameData;
import com.repositories.GameRepoMock;
import com.repositories.IGameRepository;
import com.repositories.RepositoryException;
import com.utils.GameState;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameEngine implements IServer {

    private GameState state;


    private IGameRepository gameRepository;

    private GameLoop gameLoop;

    private int bid_received = -1;
    private String card_received = null;

    int defaultNoThreads = 4;
    ExecutorService executor;


    public GameEngine() {
        executor = Executors.newFixedThreadPool(defaultNoThreads);
        //activePlayers = new ConcurrentHashMap<>();
        gameRepository = new GameRepoMock();
        state = GameState.ACTIVE;
        //players = new LinkedList<>();

    }


    @Override
    public synchronized Player login(String nickname, IClientObserver client) throws AppException {
        Player localPlayer = new Player(nickname);

        //verify if the nickname already exists;
        if(gameRepository.getPlayerObserver(localPlayer) != null)
            throw new AppException("The nickname is already in use");

        // verify other conditions, like the room is open for joining or room is full
        verifyConditionToEntry();

        //setting host attribute
        setHost(localPlayer);

        //mark client as logged in
        try {
            gameRepository.addPlayerData(localPlayer,client);
        } catch (RepositoryException e) {
            e.printStackTrace(); // should't get here
        }

        //update Lobby data for all players
        LobbyData lobbyData = (LobbyData) createGameData("lobby");
        notifyPlayers(lobbyData);

        System.out.println("Client logged in... nickname: " + nickname + " isHost: " + localPlayer.isHost());

        return localPlayer;

    }

    private void notifyPlayers(GameData gameData) {
        for(Player player: gameRepository.getPlayers()) {
            if(gameData instanceof LobbyData || gameData instanceof StartGameData) {
                IClientObserver localClient = gameRepository.getPlayerObserver(player);
                executor.execute(() -> {
                    try {
                        localClient.update(gameData);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private GameData createGameData(String state) {
        if(state.equals("lobby")){ // lobby state
            Player host = gameRepository.getHost();
            Player[] readyPlayers = gameRepository.getActivePlayers().toArray(new Player[0]);
            Player[] pendingPlayers = gameRepository.getPendingPlayers().toArray(new Player[0]);

            LobbyData lobbyData = new LobbyData(host,readyPlayers,pendingPlayers);
            return lobbyData;
        }

        if(state.equals("startGame")){ // game state
            try {
                gameRepository.shuffleOrder();
            } catch (RepositoryException e) {
                e.printStackTrace(); // should't get here, but to be sure
            }

            List<Player> players = gameRepository.getPlayers();

            StartGameData startGameData = new StartGameData(players.toArray(new Player[0]));
            return startGameData;

        }
        //here we are gonna have multiple states to handle

        return null;
    }

    /**
     * Setting host attribute for the new logged player if he is the first player to join room(maybe the one which created room);
     * @param localPlayer -- the logged player
     */
    private void setHost(Player localPlayer) {
        if(gameRepository.size() == 0) {
            localPlayer.setHost(true);
            localPlayer.setReady(false);
        }
        else {
            localPlayer.setHost(false);
            localPlayer.setReady(false);
        }
    }

    /**
     * verifies if the game state is ACTIVE(just in this state you can actually enter the game) and if the room is full(max 6 players)
     * @throws AppException -- this conditions are not meet;
     */
    private void verifyConditionToEntry() throws AppException {
        if(!state.equals(GameState.ACTIVE))
            throw new AppException("The game is not active!");
        if(gameRepository.size() >= 6)
            throw new AppException("The room is full!");

    }

    @Override
    public synchronized void logout(Player player) throws AppException {
        switch (state){
            case ACTIVE:
                try {
                    gameRepository.remove(player);
                    System.out.println("Client logged out... nickname: " + player.getNickname() );
                    if(player.isHost()){
                        System.out.println("Choosing another host...");
                        Player new_player_host = gameRepository.getPlayers().get(0);
                        new_player_host.setHost(true);
                        gameRepository.updatePlayer(new_player_host);
                        System.out.println("Player " + new_player_host.getNickname() + " is the new host");
                    }
                    LobbyData lobbyData = (LobbyData) createGameData("lobby");
                    notifyPlayers(lobbyData);
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try{
                    gameRepository.remove(player);
                    System.out.println("Client logged out... nickname: " + player.getNickname() );
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
                break;
        }


        //todo: if the game is currently in play, we want to end the game and show the score;



    }


    //deprecated
    private void changeHost() {
        for(Player player : gameRepository.getPlayers()) {
            player.setHost(true);
            player.setReady(true);
            try {
                gameRepository.updatePlayer(player);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            System.out.println("Player " + player.getNickname() + " is the new host!");
            return;
        }
    }

    @Override
    public void markAsActive(Player player) throws AppException {
        try {
            gameRepository.markActive(player);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        LobbyData lobbyData = (LobbyData) createGameData("lobby");
        notifyPlayers(lobbyData);
    }

    @Override
    public void startGame() throws AppException {


        state = GameState.PLAY;
        StartGameData data = (StartGameData) createGameData("startGame");
        notifyPlayers(data);

        List<Player> players = gameRepository.getPlayers();
        gameLoop=new GameLoop(players);
        gameLoop.setServer(this);
        Thread th = new Thread(gameLoop);
        th.start();

    }


    //major changes here
    @Override
    public void sendCard(String nickname, String id) {
        card_received = id;
    }

    @Override
    public void sendBid(int bid) {
        bid_received = bid;
    }


    public void sendCards(String nickname, List<Card> player_cards) throws RemoteException {
        IClientObserver localClient = gameRepository.getPlayerObserver(new Player(nickname));
        if(localClient != null)
            localClient.receiveCards(player_cards);
    }

    public void kick(Player p){
        IClientObserver localClient = gameRepository.getPlayerObserver(p);
        try {
            localClient.kick("A player disconnected from server.The game is over!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public int getBid(String nickname, int unavailableBid) throws RemoteException {

        if(bid_received == -1) {

            IClientObserver localClient = gameRepository.getPlayerObserver(new Player(nickname));
            localClient.getBidRequest(unavailableBid);
            return bid_received;
        }
        int bid = bid_received;
        bid_received = -1;
        return bid;

    }

    public String getPlayerCard(String nickname, String firstCard, String atuCard) throws RemoteException {
        if(card_received == null){
            IClientObserver localClient = gameRepository.getPlayerObserver(new Player(nickname));
            localClient.requestCard(firstCard,atuCard);
            return card_received;
        }
        //sending info to the player
        String card = card_received;
        card_received = null;
        return card;
    }

    public void sendGameStatus(List<Pair<String, Card>> players_cards) {
        for(Player player: gameRepository.getActivePlayers()){
            IClientObserver localClient = gameRepository.getPlayerObserver(player);

            executor.execute(()->{
                try {
                    localClient.sendCardsOnTable(players_cards);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            });
        }
    }

    public void notifyPlayersAboutBalance(Map<String, Pair<Integer, Integer>> playersScore) {
        for(Map.Entry<String ,Pair<Integer,Integer>> playerData: playersScore.entrySet()){
            System.out.println("Player: " + playerData.getKey() + "\t\t" +
                    "Bid: " + playerData.getValue().getKey() + "\t\t" +
                    "Made: " + playerData.getValue().getValue());
        }

        for(Player player: gameRepository.getActivePlayers()){
            IClientObserver localClient = gameRepository.getPlayerObserver(player);

            executor.execute(()->{
                try {
                    localClient.sendPlayersBalance(playersScore);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            });
        }
    }

    public void notifyPlayersAboutRound(Map<String, Integer> playerScore) {
        //sending to players the score for the round;
        //this message will make the client to clear GUI
        //maybe here we are gonna send multiple messages(ex. roundInfo)
        for(Player p: gameRepository.getActivePlayers()){
            IClientObserver localClient = gameRepository.getPlayerObserver(p);
            executor.execute(()->{
                try {
                    localClient.resetRound();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void sendBidsInfo(Map<String, Integer> bidsByPlayers) {
        for(Player p: gameRepository.getActivePlayers()){
            IClientObserver localClient = gameRepository.getPlayerObserver(p);
            executor.execute(()->{
                try {
                    localClient.updateBidsInfo(bidsByPlayers);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void sendAtuCard(Card atu) {
        String atuString = atu.getName();
        for(Player p: gameRepository.getActivePlayers()){
            IClientObserver localClient = gameRepository.getPlayerObserver(p);
            executor.execute(()->
            {
                try {
                    localClient.updateAtu(atuString);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void sendTotalScore(Map<String, Integer> playersScore) {
        for(Player p: gameRepository.getActivePlayers()){
            IClientObserver localClient = gameRepository.getPlayerObserver(p);
            executor.execute(()->{
                try {
                    localClient.updateTotalScore(playersScore);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
