package com;


import com.domain.Player;

public interface IServer {

    Player login(String nickname, IClientObserver client) throws AppException;
    void logout(Player player) throws AppException;


    void markAsActive(Player player) throws AppException;
    void startGame() throws AppException;


    void sendCard(String nickname,String id);

    void sendBid(int bid);
}
