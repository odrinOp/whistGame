package com;

import com.dto.GameData;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientObserver extends Serializable, Remote {

    //some functions for the players;

    void update(GameData data) throws RemoteException; // just for test
}
