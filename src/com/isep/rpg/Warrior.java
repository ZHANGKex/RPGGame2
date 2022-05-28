package com.isep.rpg;

//Warrior
public class Warrior extends Hero {
    public Warrior() {
        super("Warrior", 100, 0, 100);
        getPotions().add(new Potion());
        getLembas().add(new Food());
    }


    @Override
    public String toString() {
        return getName() + ":HP " + getLifePoints() + " ,armure " + getArmor()
                + " ,Axe attack power " + getWeaponDamage()
                + " ,amount of food " + getLembas().size();
    }
}
