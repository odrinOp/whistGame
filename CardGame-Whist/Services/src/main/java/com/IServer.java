package com;

import com.dto.GameData;

public interface IServer {

    Player login(String nickname, ClientObserver client) throws AppException;
    void logout(String nickname) throws AppException;


    void markAsActive() throws AppException;
    void startGame() throws AppException;






}
