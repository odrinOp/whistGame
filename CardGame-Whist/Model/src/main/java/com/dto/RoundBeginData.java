package com.dto;

import com.domain.Card;

import java.util.List;

public class RoundBeginData implements GameData{
    private int round;
    private Card[] cards;

    public RoundBeginData(int round, Card[] cards) {
        this.round = round;
        this.cards = cards;
    }

    public int getRound() {
        return round;
    }

    public Card[] getCards() {
        return cards;
    }
}
