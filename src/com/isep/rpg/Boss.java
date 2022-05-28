package com.isep.rpg;

public class Boss extends Enemy {
    public Boss() {
        super("Boss", 500, 20, 30);
    }

    @Override
    public String toString() {
        return getName() + ":HP " + getLifePoints() + ", armure " + getArmor() + ", arme " + getWeaponDamage();
    }
}
