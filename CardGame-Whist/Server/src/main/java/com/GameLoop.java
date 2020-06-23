package com;

import com.domain.Card;
import com.domain.Deck;
import com.domain.Player;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.*;

public class GameLoop implements Runnable {

    private Deck deck;
    private List<Player> players;
    private GameEngine server;
    private int round = 1;
    private int max_round = 2;
    private int noCards = 4;
    private TableScore score;
    private WhistRules gameRules;

    public GameLoop(List<Player> players) {
        this.players = players;
        gameRules = new WhistRules();
    }



    public void setServer(GameEngine server){
        this.server = server;

    }

    @Override
    public void run() {
        //init variables for the game
        deck = new Deck(players.size() * 8);
        score = new TableScore();
        round = 1; // todo: Create a RoundRepo
        noCards = 8;// this will be available in RoundRepo

        //starting the actual game; this will run until all rounds are completed
        while(round < max_round) {
            deck.shuffle();
            //draw and send cards for the players
            for (Player p : players) {
                List<Card> player_cards = deck.drawCards(noCards);
                try {
                    server.sendCards(p.getNickname(), player_cards);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
            //todo: add ATU Card
            /*
            Card atu = null;
            if(noCards < 8){
                atu =  deck.drawCards(1).get(0);
            }
            */
            //waiting for biddings
            for (Player p : players) {
                int bid = server.getBid(p.getNickname());
                while (bid == -1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    bid = server.getBid(p.getNickname());
                }
                score.updateData(round,p.getNickname(),"bid",bid);
            }


            List<Player> copyPlayerList = players;

            //the round will be completed when all players remain without cards
            while (noCards > 0){
                List<Pair<String ,Card >> players_cards = new LinkedList<>();

                //wainting for players to send a card
                for(Player p: copyPlayerList){
                    String playerCard = server.getPlayerCard(p.getNickname());
                    while (cardStringIsValid(playerCard))
                    {
                        try {
                            Thread.sleep(1000);
                            playerCard = server.getPlayerCard(p.getNickname());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                        playerCard = server.getPlayerCard(p.getNickname());
                    }

                    Card c = convertToCard(playerCard);
                    players_cards.add(new Pair<>(p.getNickname(),c));

                    //sending the cards to the players
                    server.sendGameStatus(players_cards);
                }

                //decide who wins the hand
                String playerName = gameRules.getWinningPlayer(players_cards,null);

                //update the score for the winner; made = made + 1
                score.updateData(round,playerName,"made",score.getMade(round,playerName) + 1);

                //notify all players about the balance for each opponent; balance = bids/made
                server.notifyPlayersAboutBalance(getPlayersBalance(round));

                //the winner will start the next hand
                copyPlayerList = setNewOrder(copyPlayerList,playerName);
                noCards -=1;
            }

            Map<String,Integer> playerScore = getPlayersScore();
            //setting the new order for the next round; first player will be the last
            players = setNewOrder(players, players.get(1).getNickname());
            //notify players about score for them and for their opponent
            server.notifyPlayersAboutRound(playerScore);

            //remaking the deck to his initial state
            deck.remakeDeck();
        }

    }

    private List<Player> setNewOrder(List<Player> players, String playerName) {
        while (!players.get(0).getNickname().equals(playerName)){
            Player p = players.get(0);
            players.remove(0);
            players.add(p);
        }
        return players;
    }

    private Map<String, Pair<Integer,Integer>> getPlayersBalance(int round) {

        Map<String, Pair<Integer,Integer>> playersBalance = new HashMap<>();
        for(Player p: players){
            int bids = score.getBid(round,p.getNickname());
            int made = score.getMade(round,p.getNickname());
            playersBalance.put(p.getNickname(),new Pair<>(bids,made));
        }
        return playersBalance;
    }

    private Map<String,Integer> getPlayersScore() {
        Map<String, Integer> playersScore = new HashMap<>();
        for(Player p: players){
            int total_score = score.calculateTotalScore(p.getNickname());
            playersScore.put(p.getNickname(),total_score);
        }
        return playersScore;
    }

    private Card convertToCard(String playerCard) {
        String[] values = playerCard.split("-");
        String type = values[0];
        switch (type){
            case "d": type="diamonds";break;
            case "h": type="hearts";break;
            case "c": type="clubs";break;
            default: type="spades";break;
        }

        int value = Integer.parseInt(values[1]);
        return new Card(value,type);
    }

    private boolean cardStringIsValid(String playerCard) {
        return !playerCard.equals("undifined");
    }


}
