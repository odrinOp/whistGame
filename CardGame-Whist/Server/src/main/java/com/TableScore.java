package com;

import com.domain.Player;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class TableScore {

    private LinkedList<RowData> rows;


    public TableScore() {
        this.rows = new LinkedList<>();
    }

    public void addRow(int round,String player){
        RowData data = new RowData(round,player);
        if(rows.contains(data)) return;
        rows.add(data);
    }

    public void clear(){
        this.rows = new LinkedList<>();
    }

    public void updateData(int round,String name,String type,int value){
        RowData localData = new RowData(round,name);
        for(RowData data: rows){
            if(data.equals(localData))
            {
                if(type.equals("bids"))
                {
                    data.setBids(value);
                    return;
                }
                else if(type.equals("made"))
                {
                    data.setMade(value);
                    return;
                }
                return;
            }
        }
    }

    public int getScore(int round,String name){
        for(RowData data: rows){
            if(data.equals(new RowData(round,name))){
                int score = Math.abs(data.bids-data.made);
                if(data.bids == -1 || data.made == -1)
                    return -1;
                if(score == 0)
                    score = 5 + data.bids;

                return score;
            }
        }
        return -1;
    }

    public int calculateTotalScore(String name){
        int score = -1;
        for(RowData data: rows){
            if(data.getPlayer().equals(name)){
                int round_score = getScore(data.getRound(),name);
                if(round_score != -1)
                    score += round_score;
            }
        }
        return score;
    }

    public int getBidSumPerRound(int round){
        int sum = 0;
        for(RowData data: rows){
            if(data.getRound() == round){
                int score = data.getBids();
                if(score == -1)
                    score = 0;

                sum += 0;
            }
        }
        return sum;
    }

    public int getBid(int round,String name){
        RowData localData = new RowData(round,name);
        for(RowData data: rows)
        {
            if(data.equals(localData)){
                return data.getBids();
            }
        }
        return -1;
    }

    public int getMade(int round,String name){
        RowData localData = new RowData(round,name);
        for(RowData data: rows)
        {
            if(data.equals(localData)){
                return data.getMade();
            }
        }
        return -1;
    }

    class RowData{
        private int round;
        private String player;
        private int bids = 0;
        private int made = 0;


        public RowData(int round, String player) {
            this.round = round;
            this.player = player;
        }

        public int getRound() {
            return round;
        }


        public String getPlayer() {
            return player;
        }

        public int getBids() {
            return bids;
        }

        public void setBids(int bids) {
            this.bids = bids;
        }

        public int getMade() {
            return made;
        }

        public void setMade(int made) {
            this.made = made;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RowData rowData = (RowData) o;

            if (round != rowData.round) return false;
            return player != null ? player.equals(rowData.player) : rowData.player == null;
        }

        @Override
        public int hashCode() {
            int result = round;
            result = 31 * result + (player != null ? player.hashCode() : 0);
            return result;
        }
    }


}
