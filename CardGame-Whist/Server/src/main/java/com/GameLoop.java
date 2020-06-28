package com;

import com.domain.Card;
import com.domain.Deck;
import com.domain.Player;
import com.repositories.RoundRepository;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.*;

public class GameLoop implements Runnable {

    private Deck deck;
    private List<Player> players;
    private GameEngine server;

    private TableScore score;
    private WhistRules gameRules;
    private RoundRepository rounds;

    public GameLoop(List<Player> players) {
        this.players = players;
        gameRules = new WhistRules();
        rounds = new RoundRepository(players.size());
    }



    public void setServer(GameEngine server){
        this.server = server;

    }

    @Override
    public void run() {
        //init variables for the game
        deck = new Deck(players.size() * 8);
        score = new TableScore();
        int noCards = 0;
        rounds.startIterator();

        String atuCard = null;
        //starting the actual game; this will run until all rounds are completed
        while(rounds.valid()) {
            atuCard = null;
            deck.shuffle();
            noCards = rounds.getNoCards();
            int id = rounds.getID();
            //draw and send cards for the players
            for (Player p : players) {
                score.addRow(id,p.getNickname());
                System.out.println("Send cards to: " + p.getNickname());
                List<Card> player_cards = deck.drawCards(noCards);
                try {
                    server.sendCards(p.getNickname(), player_cards);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
            //todo: add ATU Card
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Card atu = null;
            if(noCards < 8){
                atu =  deck.drawCards(1).get(0);
                atuCard = atu.getName();
                System.out.println("Atu will be: " + atu.getType());
                server.sendAtuCard(atu);
            }



            //waiting for biddings
            Map<String ,Integer> bidsByPlayers = new HashMap<>();
            for (Player p : players) {
                int bid = 0;
                System.out.println("Waiting for " + p.getNickname() + " to send bid!");
                try {
                    bid = server.getBid(p.getNickname(), bidUnavailableValue(bidsByPlayers,noCards));
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                int i = 0;
                while (bid == -1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        bid = server.getBid(p.getNickname(), bidUnavailableValue(bidsByPlayers, noCards));

                        i+=1;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Player " + p.getNickname() + " send bid=" + bid );
                score.updateData(id,p.getNickname(),"bid",bid);
                bidsByPlayers.put(p.getNickname(),bid);
                server.sendBidsInfo(bidsByPlayers);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            List<Player> copyPlayerList = new LinkedList<>(players);

            //the round will be completed when all players remain without cards
            while (noCards > 0){
                List<Pair<String ,Card >> players_cards = new LinkedList<>();
                String firstCard = null;
                //wainting for players to send a card
                for(Player p: copyPlayerList){
                    String playerCard = null;
                    int i = 1;
                    System.out.println("Waiting for player " + p.getNickname() + " to send card");
                    while (cardStringIsValid(playerCard))
                    {
                        try {
                            //System.out.println("Waiting for player " + p.getNickname() + " to send a card!Try: " + i);
                            Thread.sleep(1000);
                            playerCard = server.getPlayerCard(p.getNickname(), firstCard, atuCard);
                            i+=1;
                        } catch (InterruptedException | RemoteException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    if(firstCard == null)
                        firstCard = playerCard;
                    System.out.println("Player " + p.getNickname() + " send card=" + playerCard);
                    Card c = convertToCard(playerCard);
                    players_cards.add(new Pair<>(p.getNickname(),c));

                    //sending the cards to the players
                    server.sendGameStatus(players_cards);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //decide who wins the hand
                String playerName = gameRules.getWinningPlayer(players_cards,atu);
                System.out.println("Player " + playerName +" won the hand!");
                //update the score for the winner; made = made + 1
                score.updateData(id,playerName,"made",score.getMade(id,playerName) + 1);

                //notify all players about the balance for each opponent; balance = bids/made

                server.notifyPlayersAboutBalance(getPlayersBalance(id));

                //the winner will start the next hand
                copyPlayerList = setNewOrder(copyPlayerList,playerName);
                noCards -=1;

            }

            Map<String,Integer> playerScore = getPlayersScore();
            //setting the new order for the next round; first player will be the last
            players = setNewOrder(players, players.get(1).getNickname());
            //notify players about score for them and for their opponent
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            server.notifyPlayersAboutRound(playerScore);

            //remaking the deck to his initial state

            System.out.println("Round " + id +" finished!Score: ");
            Map<String,Integer> playersScore = new HashMap<>();
            for(Player p: players){
                int s = score.calculateTotalScore(p.getNickname());
                playerScore.put(p.getNickname(),s);
                System.out.println("Player: " + p.getNickname() + "\t\tScore: " + s);
            }

            server.sendTotalScore(playersScore);
            deck.remakeDeck();
            rounds.next();
        }

        System.out.println("Game has ended!");


    }

    private int bidUnavailableValue(Map<String, Integer> bidsByPlayers, int noCards) {
        if(bidsByPlayers.size() != players.size() -1)
            return -1;

        int total_bids = 0;
        for(Integer bid: bidsByPlayers.values()){
            total_bids += bid;
            if(total_bids > noCards)
                return -1;
        }

        return noCards - total_bids;
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
        return playerCard == null;
    }


}
