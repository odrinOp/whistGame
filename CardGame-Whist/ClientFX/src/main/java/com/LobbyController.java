package com;

import com.dto.GameData;
import com.dto.LobbyData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LobbyController extends UnicastRemoteObject implements IClientObserver {

    private IServer server;
    private Player player;

    @FXML
    private ListView<String> readyList;
    @FXML
    private ListView<String> pendingList;
    @FXML
    private Button readyBtn;
    @FXML
    private Button startBtn;


    public LobbyController() throws RemoteException {
    }

    @Override
    public void update(GameData data) throws RemoteException {
        if(data instanceof LobbyData){
            LobbyData lobbyData = (LobbyData) data;
            updateReadyList(lobbyData.getPlayers());
            updatePendingList(lobbyData.getPlayers());
            updateButtons();
        }
    }

    private void updateButtons() {
        readyBtn.setDisable(player.isReady());

        if(everyoneReady() && player.isHost()){
            startBtn.setDisable(false);
        }
    }

    private boolean everyoneReady() {
        return pendingList.getItems().size() == 0 && readyList.getItems().size()>1;
    }

    private void updatePendingList(Player[] players) {
        pendingList.getItems().clear();
        for(Player player: players){
            if (!player.isReady())
                pendingList.getItems().add(player.getNickname());
        }
    }

    private void updateReadyList(Player[] players) {
        readyList.getItems().clear();
        for (Player player: players){
            if(player.isReady())
                readyList.getItems().add(player.getNickname());
        }
    }

    public void initState() {
        startBtn.setDisable(true);
        if(player.isReady())
            readyBtn.setDisable(true);
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @FXML
    private void markAsActive(){
        try {
            server.markAsActive(player);
            player.setReady(true);
        } catch (AppException appException) {
            appException.printStackTrace();
        }
    }
}
