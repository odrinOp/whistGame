package com.dto;

import com.domain.Card;
import com.domain.Player;

import java.util.Map;

public class StartGameData implements GameData{

    private Player[] players;

    public StartGameData(Player[] players) {
        this.players = players;

    }

    public Player[] getPlayers() {
        return players;
    }
}
