package com;

import com.domain.Player;
import com.dto.GameData;
import com.dto.LobbyData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LobbyController {



    private MainController mainController;

    @FXML
    private ListView<String> readyList;
    @FXML
    private ListView<String> pendingList;
    @FXML
    private Button readyBtn;
    @FXML
    private Button startBtn;
    @FXML
    private Label hostTxt;



    public LobbyController() throws RemoteException {
    }

    public void update(GameData data) throws RemoteException {
        if(data instanceof LobbyData) {
            LobbyData lobbyData = (LobbyData) data;
            updateHost(lobbyData.getHost());
            updateReadyList(lobbyData.getReadyPlayers());
            updatePendingList(lobbyData.getPendingPlayers());
            updateButtons();
        }


    }

    private void updateHost(Player player) {
        if(player.isHost()){
            hostTxt.setText("Host: " + player.getNickname());
            if(player.equals(mainController.getPlayer())){
                mainController.setPlayer(player);
            }
        }
    }

    private void updateButtons() {
        readyBtn.setDisable(mainController.getPlayer().isReady());

        if(everyoneReady() && mainController.getPlayer().isHost()){
            startBtn.setDisable(false);
        }



    }

    private boolean everyoneReady() {
        return pendingList.getItems().size() == 0 && readyList.getItems().size()>1;
    }

    private void updatePendingList(Player[] players) {
        pendingList.getItems().clear();
        for(Player player: players){
            pendingList.getItems().add(player.getNickname());
        }
    }

    private void updateReadyList(Player[] players) {
        readyList.getItems().clear();
        for (Player player: players){
            readyList.getItems().add(player.getNickname());
        }
    }

    public void initState() {
        startBtn.setDisable(true);
        if(mainController.getPlayer().isReady())
            readyBtn.setDisable(true);
    }



    @FXML
    private void markAsActive(){
        try {
            mainController.markAsReady();
        } catch (AppException appException) {
            appException.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void startGame(){
        mainController.startGame();
    }
}
