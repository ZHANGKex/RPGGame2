package com.isep.rpg;

public class Food implements Consumable{
    private String name;
    private int effect;

    public Food() {
        this.name = "food";
        this.effect = 5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEffect() {
        return effect;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }
}
