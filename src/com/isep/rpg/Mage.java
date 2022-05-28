package com.isep.rpg;

//Mage
public class Mage extends SpellCaster {
    public Mage() {
        super("Mage", 60, 0, 5);
        setManaPoints(30);
        getPotions().add(new Potion());
        getLembas().add(new Food());
    }

    @Override
    public int attack(Enemy enemy) {
        enemy.setLifePoints(enemy.getLifePoints() - getWeaponDamage());
        //Magic can ignore armor
        return getWeaponDamage();
    }

    @Override
    public String toString() {
        return getName() + ":HP " + getLifePoints() + " ,armure " + getArmor() + " ,magic " + getManaPoints()
                + " ,magic attack " + getWeaponDamage()
                + " ,amount of food " + getLembas().size()
                + " ,amount of potion " + getPotions().size();
    }
}
