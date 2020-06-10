package com;


import com.dto.GameData;
import com.dto.LobbyData;
import com.utils.GameState;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameEngine implements IServer {

    private GameState state;
    private Map<Player, IClientObserver> activePlayers;
    private WhistRules gameRules;

    int defaultNoThreads = 4;


    public GameEngine() {
        activePlayers = new ConcurrentHashMap<>();
        gameRules = new WhistRules();
        state = GameState.ACTIVE;
    }


    @Override
    public synchronized Player login(String nickname, IClientObserver client) throws AppException {
        Player localPlayer = new Player(nickname);

        //verify if the nickname already exists;
        if(activePlayers.get(localPlayer) != null)
            throw new AppException("The nickname already exists!");

        // verify other conditions, like the room is open for joining or room is full
        verifyConditionToEntry();

        //setting host attribute
        setHost(localPlayer);

        //mark client as logged in
        activePlayers.put(localPlayer,client);

        //update Lobby data for all players
        LobbyData lobbyData = (LobbyData) createGameData("lobby");
        notifyPlayers(lobbyData);

        System.out.println("Client logged in... nickame: " + nickname + " isHost: " + localPlayer.isHost());

        return localPlayer;

    }

    private void notifyPlayers(GameData gameData) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultNoThreads);
        for(IClientObserver player : activePlayers.values())
            executor.execute(()-> {
                try {
                    player.update(gameData);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
    }

    private GameData createGameData(String state) {
        if(state.equals("lobby")){ // lobby state
            LobbyData lobbyData = new LobbyData(activePlayers.keySet().toArray(new Player[0]));
            return lobbyData;
        }

        //here we are gonna have multiple states to handle

        return null;
    }

    /**
     * Setting host attribute for the new logged player if he is the first player to join room(maybe the one which created room);
     * @param localPlayer -- the logged player
     */
    private void setHost(Player localPlayer) {
        if(activePlayers.size() == 0) {
            localPlayer.setHost(true);
            localPlayer.setReady(true);
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
        if(activePlayers.size() >= 6)
            throw new AppException("The room is full!");

    }

    @Override
    public void logout(Player player) throws AppException {
        //verify if the player is currently logged in
        if(activePlayers.get(player) == null)
            throw new AppException("You are not currently logged in!\nThis error shouldn't happen!\nCheck server!");

        //todo: if the game is currently in play, we want to end the game and show the score;
        activePlayers.remove(player);
        System.out.println("Client logged out... nickame: " + player.getNickname() );

    }

    @Override
    public void markAsActive(Player player) throws AppException {
        //in order to update a key from map, we need to delete entry, then adding again with new attr.
        IClientObserver client = activePlayers.remove(player);

        if(client == null)
            throw new AppException("You are not currently logged in!\nThis error shouldn't happen!\nCheck server!");

        //updating and adding back the entry
        player.setReady(true);
        activePlayers.put(player,client);

        //creating the gameData to notify all

        LobbyData lobbyData = (LobbyData) createGameData("lobby");
        notifyPlayers(lobbyData);
    }

    @Override
    public void startGame() throws AppException {

    }
}
