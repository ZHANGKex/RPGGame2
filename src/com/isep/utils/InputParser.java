package com.isep.utils;

import com.isep.rpg.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InputParser {
    private Game game;
    private int enemyActionCount = 0;
    //Statistics of the number of enemy actions, used for the enemy to act in order

    public InputParser(Game game) {
        this.game = game;
    }

    public Hero addHero(String type) {
        Hero hero = null;
        if ("Mage".equals(type)) {
            hero = new Mage();
            game.getHeroes().add(hero);
        } else if ("Healer".equals(type)) {
            hero = new Healer();
            game.getHeroes().add(hero);
        } else if ("Hunter".equals(type)) {
            hero = new Hunter();
            game.getHeroes().add(hero);
        } else if ("Warrior".equals(type)) {
            hero = new Warrior();
            game.getHeroes().add(hero);
        }
        return hero;
    }

    /**
     * Parse hero input
     *
     * @param hero
     * @param selectedItem
     * @param enemy
     * @return
     */
    public String[] parser(Hero hero, String selectedItem, Enemy enemy) {
        String[] result = new String[]{"", ""};
        if (hero.getLifePoints() <= 0) {
            result[0] = "fail";
            result[1] = hero.getName() + "This hero is dead, please choose another hero";
            return result;
        }
        if ("potion".equals(selectedItem)) {
            if (hero instanceof Hunter || hero instanceof Warrior) {
                result[0] = "fail";
                result[1] = "you are" + hero.getName() + " ,You can't drink potions, please choose again";
            } else {
                List<Potion> potions = hero.getPotions();
                if (potions.size() > 0) {
                    result[0] = "success";
                    Potion remove = potions.remove(0);
                    hero.useConsumable(remove);
                    result[1] = hero.getName() + "Use potions, ++ " + remove.getEffect() + " magic";
                } else {
                    result[0] = "fail";
                    result[1] = hero.getName() + "Your potion has run out, please choose another action";
                }
            }
        } else if ("food".equals(selectedItem)) {
            List<Food> lembas = hero.getLembas();
            if (lembas.size() > 0) {
                result[0] = "success";
                Food remove = lembas.remove(0);
                hero.useConsumable(remove);
                result[1] = hero.getName() + "use food, ++ " + remove.getEffect() + " life point";
            } else {
                result[0] = "fail";
                result[1] = hero.getName() + " You are out of food, please choose another action ";
            }
        } else if ("attack".equals(selectedItem)) {
            if (enemy == null) {
                result[0] = "fail";
                result[1] = "please choose a enemy !!";
            } else {
                result[0] = "success";
                int attack = hero.attack(enemy);
                result[1] = hero.getName() + "attacked the enemy " + enemy.getName() + " ,-- his HP" + attack;
                if (hero instanceof Hunter) {
                    int arrows = ((Hunter) hero).getArrows();
                    if (arrows == 0) {
                        result[1] += " , Arrows have run out";
                    }
                }
                if (enemy.getLifePoints() <= 0) {
                    game.getDeadEnemyTurn().add(enemy);
                }
            }
        } else if ("cure".equals(selectedItem)) {
            if (hero instanceof Healer) {
                Healer healer = (Healer) hero;
                List<Hero> heroes = game.getHeroes();
                Hero hero2 = heroes.stream().min(Comparator.comparingInt(Hero::getLifePoints)).get();
                boolean heal = healer.heal(hero2);
                if (heal) {
                    result[0] = "success";
                    result[1] = hero.getName() + "spent 5 magic to heal the teammate with the lowest health " + hero2.getName() + " ,++5 teammate's HP";
                } else {
                    result[0] = "fail";
                    result[1] = "Insufficient magic to heal, please reselect";
                }
            } else {
                result[0] = "fail";
                result[1] = "Only Healer can heal Hero";
            }
        } else if ("defense".equals(selectedItem)) {
            hero.defend();
            result[0] = "success";
            result[1] = hero.getName() + "Selected to increase armor, defense value ++ 5";
        }
        return result;
    }

    /**
     * enemy turn
     *
     * @return
     */
    public String enemyAction() {
        //Get enemies with HP > 0
        List<Enemy> enemies = game.getEnemies().stream().filter(e -> e.getLifePoints() > 0).collect(Collectors.toList());
        Enemy enemy = enemies.get(enemyActionCount % enemies.size());
        enemyActionCount++;
        if (enemy.getName().equals("Healer")) {
            Enemy enemy1 = enemies.stream().findAny().get();
            enemy1.setLifePoints(enemy1.getLifePoints() + 5);
            return "enemy" + enemy.getName() + "treated" + enemy1.getName() + " ,++ its HP" + 5;
        } else {
            //The enemy gets a random hero to attack
            Hero hero = game.getHeroes().stream().filter(h -> h.getLifePoints() > 0).findAny().get();
            int armor = hero.getDefend() + hero.getArmor();
            int hurt = Math.max(enemy.getWeaponDamage() - armor, 0);//attect
            hero.setLifePoints(hero.getLifePoints() - hurt);
            if (hero.getLifePoints() <= 0) {
                game.getDeadHeroTurn().add(hero);
            }
            return "Hero：\n" + enemy.getName() + " attacked our hero's " + hero.getName() + " ,caused" + hero.getName() + " HP -- " + hurt;
        }
    }

    public String getInfo() {
        String heroStr = game.getHeroes().stream().map(Object::toString).collect(Collectors.joining("\n"));
        String enemyStr = game.getEnemies().stream().map(Object::toString).collect(Collectors.joining("\n"));
        return "Hero：\n" + heroStr + "\nenemy：\n" + enemyStr;
    }

    /**
     * "Increase Defense", "Increase Damage", "Increase Potions and Food"
     *
     * @param selectedItem
     * @return
     */
    public String awardHero(String selectedItem) {
        game.getHeroes().stream().filter(h -> h.getLifePoints() > 0)
                .forEach(h -> {
                    if (selectedItem.equals("increase armor")) {
                        h.setArmor(h.getArmor() + 5);
                    } else if (selectedItem.equals("increase attack power")) {
                        h.setWeaponDamage(h.getWeaponDamage() + 3);
                    } else {
                        h.getLembas().add(new Food());
                        h.getPotions().add(new Potion());
                    }
                });
        return "defeats the enemy in this round, all heroes" + selectedItem;
    }
}
