package com.repositories.dto;

import com.IClientObserver;
import com.domain.Player;

public class GamePlayerData {
    Player player;
    IClientObserver observer;
    private boolean active = false;

    public GamePlayerData(Player player, IClientObserver observer) {
        this.player = player;
        this.observer = observer;
    }

    public Player getPlayer() {
        return player;
    }

    public IClientObserver getObserver() {
        return observer;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setObserver(IClientObserver observer) {
        this.observer = observer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
