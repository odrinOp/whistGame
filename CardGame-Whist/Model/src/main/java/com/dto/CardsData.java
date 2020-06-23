package com.dto;

import com.domain.Card;

public class CardsData implements GameData{
    Card[] cards;

    public CardsData(Card[] cards) {
        this.cards = cards;
    }

    public Card[] getCards() {
        return cards;
    }

}
