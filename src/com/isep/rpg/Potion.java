package com.isep.rpg;

//Potion
public class Potion implements Consumable {
    private String name;
    private int effect;

    public Potion() {
        this.name = "Potion";
        this.effect = 10;
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
