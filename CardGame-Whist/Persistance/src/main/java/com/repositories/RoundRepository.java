package com.repositories;

import java.util.LinkedList;

public class RoundRepository {
    private LinkedList<Data> rounds;
    private Iterator iterator;

    public RoundRepository(int noOfPlayers) {
        this.rounds = generateRounds(noOfPlayers);
        this.iterator = null;
    }

    private LinkedList<Data> generateRounds(int noOfPlayers) {
        int id = 1;
        int noOfCards = 1;
        LinkedList<Data> rounds = new LinkedList<>();

        for(int i=0; i<noOfPlayers;i++){
            Data d = new Data(id,noOfCards);
            rounds.add(d);
            id++;
        }
        noOfCards +=1;

        while (noOfCards < 8){
            Data d = new Data(id,noOfCards);
            rounds.add(d);
            id++;
            noOfCards +=1;
        }

        for(int i=0; i<noOfPlayers;i++){
            Data d = new Data(id,noOfCards);
            rounds.add(d);
            id++;
        }

        noOfCards -=1;
        while (noOfCards > 1){
            Data d = new Data(id,noOfCards);
            rounds.add(d);
            id++;
            noOfCards -=1;
        }

        for(int i=0; i<noOfPlayers;i++){
            Data d = new Data(id,noOfCards);
            rounds.add(d);
            id++;
        }
        return rounds;
    }

    public void startIterator(){
        if (iterator == null)
            iterator = new Iterator();
        else iterator.reset();
    }


    public void next(){
        iterator.next();
    }

    public boolean valid(){
        return iterator.isValid();
    }

    public int getID(){
        return iterator.getId();
    }

    public int getNoCards(){
        return iterator.getNoCards();
    }

    private class Data{
        private int id;
        private int noCards;

        public Data(int id, int noCards) {
            this.id = id;
            this.noCards = noCards;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNoCards() {
            return noCards;
        }

        public void setNoCards(int noCards) {
            this.noCards = noCards;
        }
    }

    class Iterator{
        int index = 0;

        public void reset(){
            this.index = 0;
        }

        public boolean isValid(){
            return index < rounds.size();
        }

        public void next(){
            if (isValid())
                index +=1;
        }

        public int getId(){
            return rounds.get(index).id;
        }

        int getNoCards(){
            return rounds.get(index).noCards;
        }
    }
}
