package com;

import com.domain.Card;
import javafx.util.Pair;

import java.util.List;

public class WhistRules {
    public String getWinningPlayer(List<Pair<String, Card>> players_cards, Card atu) {
        Card winningCard = null;
        String winningPlayer ="none";
        for(Pair<String ,Card> pair : players_cards){
            if(winningCard == null){
                winningCard = pair.getValue();
                winningPlayer = pair.getKey();
                continue;
            }

            Card c = pair.getValue();
            String p = pair.getKey();

            if(atu == null){
                if(c.getType().equals(winningCard.getType()) && c.getValue() > winningCard.getValue())
                {
                    winningCard = c;
                    winningPlayer = p;
                    continue;
                }
            }
            else{
                if (c.getType().equals(atu.getType()) && winningCard.getType().equals(atu.getType())){
                    if(c.getValue() > winningCard.getValue())
                    {
                        winningCard = c;
                        winningPlayer = p;
                        continue;
                    }
                }
                else if(c.getType().equals(atu.getType()) && !winningCard.getType().equals(atu.getType())){
                    winningCard = c;
                    winningPlayer = p;
                    continue;
                }

                else if(!c.getType().equals(atu.getType()) && c.getType().equals(winningCard.getType())){
                    if(c.getValue() > winningCard.getValue())
                    {
                        winningCard = c;
                        winningPlayer = p;
                        continue;
                    }
                }

            }

        }
        return winningPlayer;
    }
}
