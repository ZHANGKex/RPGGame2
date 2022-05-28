package com.isep.rpg;

//Healer
public class Healer extends SpellCaster {
    public Healer() {
        super("Healer", 80, 0, 2);
        setManaPoints(30);
        getPotions().add(new Potion());
        getLembas().add(new Food());
    }

    /**
     * Healing Heroes
     *
     * @param hero
     */
    public boolean heal(Hero hero) {
        int manaPoints = getManaPoints();
        if (manaPoints > 5) {
            //reduce magic
            setManaPoints(manaPoints - 5);
            //Heal hero, add 5 HP
            hero.setLifePoints(hero.getLifePoints() + 5);
            return true;
        }
        //Insufficient magic, treatment failed
        return false;
    }

    @Override
    public String toString() {
        return getName() + ":HP " + getLifePoints() + " ,armure " + getArmor()+ " ,magic " + getManaPoints()
                + " ,arme " + getWeaponDamage()
                + " ,amount of food " + getLembas().size()
                + " ,amount of potion " + getPotions().size();
    }
}
