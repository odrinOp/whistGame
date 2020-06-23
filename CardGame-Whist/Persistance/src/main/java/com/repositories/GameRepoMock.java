package com.repositories;

import com.IClientObserver;
import com.domain.Player;
import com.repositories.dto.GamePlayerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameRepoMock implements IGameRepository {
    private List<GamePlayerData> gamePlayerData;

    public GameRepoMock() {
        this.gamePlayerData = new LinkedList<>();
    }


    public List<GamePlayerData> getGamePlayerData() {
        return gamePlayerData;
    }
    @Override
    public void addPlayerData(Player player, IClientObserver client) throws RepositoryException {
        if(contains(player))
            throw new RepositoryException();
        GamePlayerData data = new GamePlayerData(player,client);
        gamePlayerData.add(data);
    }

    private boolean contains(Player player) {
        for(GamePlayerData data: gamePlayerData){
            if(data.getPlayer().getNickname().equals(player.getNickname()))
                return true;
        }
        return false;
    }
    @Override
    public IClientObserver getPlayerObserver(Player player){
        for(GamePlayerData data: gamePlayerData){
            if(data.getPlayer().getNickname().equals(player.getNickname()))
                return data.getObserver();
        }
        return null;
    }

    @Override
    public List<Player> getPlayers(){
        List<Player> players = new LinkedList<>();
        for(GamePlayerData data: gamePlayerData){
            players.add(data.getPlayer());
        }
        return players;
    }

    @Override
    public void updatePlayer(Player player) throws RepositoryException {
        for(GamePlayerData data: gamePlayerData){
            if (data.getPlayer().getNickname().equals(player.getNickname())){
                player.setHost(player.isHost());
                player.setReady(player.isReady());
                return;
            }
        }
        throw new RepositoryException();
    }

    @Override
    public void shuffleOrder() throws RepositoryException {
        Collections.shuffle(gamePlayerData);
    }

    @Override
    public void updateOrder() throws RepositoryException {
        //we are gonna put the first player into the last position
        GamePlayerData data = gamePlayerData.remove(0);
        gamePlayerData.add(data);
    }

    @Override
    public void remove(Player player) throws RepositoryException {
        for(GamePlayerData playerData : gamePlayerData){
            if(playerData.getPlayer().getNickname().equals(player.getNickname()))
            {
                gamePlayerData.remove(playerData);
                return;
            }
        }
        throw new RepositoryException();
    }

    @Override
    public void markActive(Player player) throws RepositoryException {
        for (GamePlayerData playerData: gamePlayerData)
        {
            if(playerData.getPlayer().getNickname().equals(player.getNickname())) {
                playerData.setActive(true);
                return;
            }
        }
        throw new RepositoryException();
    }

    @Override
    public Player getHost() {
        for(GamePlayerData data: gamePlayerData)
            if(data.getPlayer().isHost())
                return data.getPlayer();

            return null;
    }

    @Override
    public List<Player> getActivePlayers() {
        List<Player> players = new ArrayList<>();
        for(GamePlayerData playerData: gamePlayerData){
            if(playerData.isActive())
                players.add(playerData.getPlayer());
        }
        return players;
    }

    @Override
    public List<Player> getPendingPlayers() {
        List<Player> players = new ArrayList<>();
        for(GamePlayerData playerData: gamePlayerData){
            if(!playerData.isActive())
                players.add(playerData.getPlayer());
        }
        return players;
    }

    @Override
    public Player getPlayer(String nickname) {
        for(GamePlayerData playerData: gamePlayerData){
            if(playerData.getPlayer().getNickname().equals(nickname))
                return playerData.getPlayer();
        }
        return null;
    }

    @Override
    public int size() {
        return gamePlayerData.size();
    }




}
