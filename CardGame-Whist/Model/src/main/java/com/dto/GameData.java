package com.dto;

import com.Card;
import com.Player;

import java.util.List;
import java.util.Map;

public class GameData {

    private String state;
    //for lobby state
    List<String> players;


    //for ACTIVE STATE
    boolean yourTurn;
    Card middleCard;
    Map<String,Card> cards;

    //for end-round state
    boolean finishedRound;

    // for end-game state
    boolean finishedGame;
    Map<String,Integer> score;


    //lobby state
    public GameData(String state, List<String> players) {
        this.state = state;
        this.players = players;
    }

    //active state
    public GameData(String state, boolean yourTurn, Card middleCard, Map<String, Card> cards) {
        this.state = state;
        this.yourTurn = yourTurn;
        this.middleCard = middleCard;
        this.cards = cards;
    }

    //finished-round or finished-game state
    public GameData(String state, boolean finished, Map<String, Integer> score) {
        this.state = state;
        if(state.equals("END-ROUND"))
            this.finishedRound = finishedRound;
        if(state.equals("END-GAME"))
            this.finishedGame = finished;

        this.score = score;
    }

    public String getState() {
        return state;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public Card getMiddleCard() {
        return middleCard;
    }

    public Map<String, Card> getCards() {
        return cards;
    }

    public boolean isFinishedRound() {
        return finishedRound;
    }

    public boolean isFinishedGame() {
        return finishedGame;
    }

    public Map<String, Integer> getScore() {
        return score;
    }
}
