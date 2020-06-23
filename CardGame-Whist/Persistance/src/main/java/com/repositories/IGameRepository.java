package com.repositories;

import com.IClientObserver;
import com.domain.Player;

import java.util.List;

public interface IGameRepository {

    void addPlayerData(Player player, IClientObserver client) throws RepositoryException;
    void updatePlayer(Player player) throws RepositoryException;
    void remove(Player player) throws RepositoryException;
    Player getPlayer(String nickname);
    List<Player> getPlayers();


    List<Player> getActivePlayers();
    List<Player> getPendingPlayers();
    IClientObserver getPlayerObserver(Player player);


    void shuffleOrder() throws RepositoryException;
    void updateOrder() throws RepositoryException;

    //will be removed in later versions
    void markActive(Player player) throws RepositoryException;
    Player getHost();




    int size();
}
