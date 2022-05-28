package com.isep.rpg;

import com.isep.utils.InputParser;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private List<Hero> heroes;
    private int playerTurn = 1;
    private InputParser inputParser;

    private List<Enemy> enemies;

    private List<Enemy> deadEnemyTurn;
    private List<Hero> deadHeroTurn;


    public Game() {
        heroes = new ArrayList<>();
        enemies = new ArrayList<>();
        inputParser = new InputParser(this);
        deadEnemyTurn = new ArrayList<>();
        deadHeroTurn = new ArrayList<>();
    }

//play game time to set the round to 1 to start
    public void playGame(){
        playerTurn = 1;
        deadEnemyTurn.clear();
        deadHeroTurn.clear();
    }

//Generate corresponding enemies based on heroes
    public void generateCombat(){
        enemies.clear();
        for (int i = 0; i < heroes.size(); i++) {
            enemies.add(new BasicEnemy("enemy" + (i + 1)));
        }
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public InputParser getInputParser() {
        return inputParser;
    }

    public void setInputParser(InputParser inputParser) {
        this.inputParser = inputParser;
    }

    //Get the enemies who died in the current round
     //@return
    public List<Enemy> getDeadEnemyTurn() {
        return deadEnemyTurn;
    }

    //Get the heroes who died in the current round
    public List<Hero> getDeadHeroTurn() {
        return deadHeroTurn;
    }

    /**
     * 0 draw
     * 1 hero wins
     * 2 the enemy wins
     * @return
     */
    public int getWinner() {
        if (heroes.stream().filter(h->h.getLifePoints()<=0).count() ==heroes.size()) {
            return 2;
        }
        if (enemies.stream().filter(e->e.getLifePoints()<=0).count()==enemies.size()) {
            return 1;
        }
        return 0;
    }
}
