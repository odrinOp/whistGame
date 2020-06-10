package com;

public interface IServer {

    Player login(String nickname, IClientObserver client) throws AppException;
    void logout(Player player) throws AppException;


    void markAsActive(Player player) throws AppException;
    void startGame() throws AppException;






}
