package com.dto;

import com.Player;

import java.util.Map;

public class LobbyData implements GameData {

    private Player[] players;

    public LobbyData(Player[] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return players;
    }
}
