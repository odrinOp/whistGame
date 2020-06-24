package com;






import com.domain.Card;
import com.dto.GameData;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface IClientObserver extends Serializable, Remote {

    //some functions for the players;

    void update(GameData data) throws RemoteException;

    void receiveCards(List<Card> cards) throws RemoteException;


    void kick(String s)throws RemoteException;

    void getBidRequest(int unavailableBid) throws RemoteException;

    void updateBidsInfo(Map<String, Integer> bidsByPlayers) throws RemoteException;
}