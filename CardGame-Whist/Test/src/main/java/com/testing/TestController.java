package com.testing;

import com.domain.Card;
import com.domain.Deck;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class TestController {

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private ImageReader reader;
    private Deck deck;

    public TestController() {
    }

    public void initState() throws IOException {
        gc = canvas.getGraphicsContext2D();
        //gc.setGlobalBlendMode(BlendMode.SCREEN);
        refreshCanvas(gc);
        reader = new ImageReader();
        deck = new Deck(52);
        deck.shuffle();

        List<Card> cardList = deck.drawCards(8);

        PlayerGUIController playerGUI = new PlayerGUIController(Color.GREEN,canvas.getWidth()/2,(canvas.getHeight()-20 + 320)/2);
        //OpponentGUIController oppGUI1 = new OpponentGUIController("left",reader.getCardImage("back"),4,50,canvas.getHeight()/2);
        //OpponentGUIController oppGUI2= new OpponentGUIController("right",reader.getCardImage("back"),8,50,50);

        for(Card c:cardList){
            playerGUI.addCard(c.getName(),reader.getCardImage(c.getName()));
        }

        playerGUI.showCardsName();

        playerGUI.render(gc);
        //oppGUI1.render(gc);
        //oppGUI2.render(gc);

        canvas.setOnMouseClicked(mouseEvent -> {

            playerGUI.mouseEvent(mouseEvent);
            refreshCanvas(gc);
            playerGUI.render(gc);

        });

    }

    void refreshCanvas(GraphicsContext gc){
        gc.setFill(Color.GREEN);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }




}
