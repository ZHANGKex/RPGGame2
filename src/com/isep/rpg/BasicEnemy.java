package com.isep.rpg;

public class BasicEnemy extends Enemy {
    public BasicEnemy(String name) {
        super(name, 50, 0, 5);
    }


    @Override
    public String toString() {
        return getName() + ":HP " + getLifePoints() + ", armure " + getArmor() + ", arme " + getWeaponDamage();
    }
}
