package com;

import com.domain.Card;
import javafx.util.Pair;

import java.util.List;

public class WhistRules {
    public String getWinningPlayer(List<Pair<String, Card>> players_cards, Card atu) {
        Card winningCard = null;
        String winningPlayer ="none";
        for(Pair<String ,Card> pair : players_cards){
            if (winningCard == null)
            {
                winningCard = pair.getValue();
                winningPlayer = pair.getKey();
                continue;
            }
            Card c = pair.getValue();
            String playerName = pair.getKey();
            if (atu == null){ // round of 8
                if(winningCard.getType().equals(c.getType()) && winningCard.getValue() < c.getValue()) {
                    winningCard = c;
                    winningPlayer = playerName;
                }
            }
            else{
                if(c.getType().equals(atu.getType()) && !winningCard.getType().equals(atu.getType())){
                    winningCard = c;
                    winningPlayer = playerName;
                }
                else if(winningCard.getType().equals(c.getType()) && winningCard.getValue() < c.getValue()) {
                    winningCard = c;
                    winningPlayer = playerName;
                }

            }
        }
        return winningPlayer;
    }
}
