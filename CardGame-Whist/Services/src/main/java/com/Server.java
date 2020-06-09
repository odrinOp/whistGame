package com;



public interface Server {

    void addPlayer(Player player,ClientObserver client);
    void removePlayer(Player player);

    void notifyPlayers();
}
