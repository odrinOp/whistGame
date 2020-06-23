package com.domain;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Card implements Serializable {

    private int value;
    private String type;
    private Image frontImage;
    private Image backImage;
    private boolean hidden  = false;

    public Card(int value, String type) {
        this.value = value;
        this.type = type;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName(){
        return type.charAt(0) + "-" + value;
    }
}
