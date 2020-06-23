package com.domain;

import java.io.Serializable;

public class Player implements Serializable {
    private String nickname;
    private boolean isHost;
    private boolean isReady;

    public Player(String nickname, boolean isHost,boolean isReady) {
        this.nickname = nickname;
        this.isHost = isHost;
        this.isReady = isReady;
    }

    public Player(String nickname) {
        this.nickname = nickname;
        isHost = false;
        isReady = false;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (nickname != null)
            return nickname.equals(player.nickname);

        return player.nickname == null;
    }

    @Override
    public int hashCode() {
        if (nickname != null) return nickname.hashCode();
        return 0;
    }
}
