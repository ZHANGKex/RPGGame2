package com.isep.rpg;

//Hunter
public class Hunter extends Hero {
    private int arrows;

    public Hunter() {
        super("猎人",80, 0, 3);
        this.arrows = 15;
        getPotions().add(new Potion());
        getLembas().add(new Food());
    }

    @Override
    public int attack(Enemy enemy) {
        if (arrows > 0) {
            arrows--;
            enemy.setLifePoints(enemy.getLifePoints() - 3);
            return 3;
        } else {
            return 0;
        }
    }

    public int getArrows() {
        return arrows;
    }

    public void setArrows(int arrows) {
        this.arrows = arrows;
    }
    @Override
    public String toString() {
        return getName() + ":HP " + getLifePoints() + " ,armure " + getArmor()
                + " ,Arrow attack power " + getWeaponDamage()+" number of arrows "+ arrows
                + " ,amount of food " + getLembas().size();
    }
}
