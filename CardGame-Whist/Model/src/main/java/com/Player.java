package com;

public class Player {
    private String nickname;
    private boolean isHost;

    public Player(String nickname, boolean isHost) {
        this.nickname = nickname;
        this.isHost = isHost;
    }

    public Player(String nickname) {
        this.nickname = nickname;
        isHost = false;
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
}
