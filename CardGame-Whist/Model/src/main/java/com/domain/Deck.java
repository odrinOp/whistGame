package com.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> remainingCards;
    private List<Card> dispatchCards;
    private int numOfCards;
    public final String[] types = {"hearts","spades","diamonds","clubs"};

    public Deck(int numOfCards) {
        this.numOfCards = numOfCards;
        this.remainingCards = createDeck();
        this.dispatchCards = new ArrayList<>();
    }

    private List<Card> createDeck()
    {
        List<Card> cards = new ArrayList<>();
        int count = 0;
        int card_value = 14;
        while(count < numOfCards){
            //here we create the cards
            for(String type: types){
                Card c = new Card(card_value,type);
                cards.add(c);
                count += 1;
            }
            card_value-=1;
        }
        System.out.println("Size of the deck = "  + cards.size());
        return cards;
    }


    public void shuffle(){
        Collections.shuffle(remainingCards);
    }

    public List<Card> drawCards(int numberOfCards){
        if(numberOfCards> remainingCards.size())
            return null;

        List<Card> cards = new ArrayList<>();
        for(int i = 0; i< numberOfCards ; i++){
            Card c = remainingCards.remove(0);
            dispatchCards.add(c);
            cards.add(c);
        }
        return cards;
    }

    public void remakeDeck(){
        while (dispatchCards.size() > 0){
            Card c = dispatchCards.remove(0);
            remainingCards.add(c);
        }
    }



}
