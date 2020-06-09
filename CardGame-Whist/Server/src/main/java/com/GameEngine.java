package com;


import com.dto.GameData;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameEngine implements IServer {

    private String gameState;
    private Map<String,ClientObserver> activePlayers;
    private WhistRules gameRules;

    int defaultNoThreads = 1;


    public GameEngine() {
        activePlayers = new ConcurrentHashMap<>();
        gameRules = new WhistRules();
        gameState = "OPEN";
    }

    @Override
    public Player login(String nickname, ClientObserver client) throws AppException {
        if(!gameState.equals("OPEN"))
            throw new AppException("Game is not open");

        if(activePlayers.get(nickname) != null)
            throw new AppException("This name is already in use!");


        if(activePlayers.size()== 6)
            throw new AppException("This room is already full!");

        Player player = new Player(nickname);
        activePlayers.put(nickname,client);

        if(activePlayers.size() == 1)
            player.setHost(true);

        else
            player.setHost(false);

        defaultNoThreads +=1;
        List<String> players = new LinkedList<>();
        players.addAll(activePlayers.keySet());
        GameData data = new GameData("Lobby",players);

        return player;
    }

    @Override
    public void logout(String nickname) throws AppException {
        if(activePlayers.get(nickname) == null)
            throw new AppException("You are not connected to the game");

        defaultNoThreads -= 1;
        activePlayers.remove(nickname);

        GameData date = new GameData("END-GAME",null); //todo: adding score



    }

    @Override
    public void markAsActive() {

    }


    @Override
    public void startGame() {

    }


    private void notifyPlayers(GameData data){
        System.out.println("Notify players about gameState");



        ExecutorService executor = Executors.newFixedThreadPool(defaultNoThreads);

        for(ClientObserver client: activePlayers.values()){
            executor.execute(()->{
                client.update(data);
            });
        }
    }


}
