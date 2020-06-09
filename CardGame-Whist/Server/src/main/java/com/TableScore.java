package com;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class TableScore {


    class PlayerScore{
        private Player player;
        private int total = 0;
        private List<Integer> score;
        private List<Pair<Integer,Integer>> bets;

        public PlayerScore(Player player) {
            this.player = player;
            score = new LinkedList<>();
            bets = new LinkedList<>();
        }


        public void addBet(int bet){
            Pair<Integer,Integer> currentBet = new Pair<>(bet,-1);
            bets.add(currentBet);
        }

        public void calculateScore(int bet){

        }
    }


}
