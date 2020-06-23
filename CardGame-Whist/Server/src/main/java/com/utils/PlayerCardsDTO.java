package com.utils;

import com.domain.Card;
import com.domain.Player;

import java.util.List;

public class PlayerCardsDTO {
    Player player;
    List<Card> cards;

    public PlayerCardsDTO(Player player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
