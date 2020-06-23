package com.dto;

import com.domain.Player;

public class LobbyData implements GameData {

    private Player host;
    private Player[] readyPlayers;
    private Player[] pendingPlayers;


    public LobbyData(Player host,Player[] readyPlayers, Player[] pendingPlayers) {
        this.host = host;
        this.readyPlayers = readyPlayers;
        this.pendingPlayers = pendingPlayers;
    }

    public Player[] getReadyPlayers() {
        return readyPlayers;
    }

    public Player[] getPendingPlayers() {
        return pendingPlayers;
    }

    public Player getHost() {
        return host;
    }
}
