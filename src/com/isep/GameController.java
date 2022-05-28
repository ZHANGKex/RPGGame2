package com.isep;

import com.isep.rpg.Boss;
import com.isep.rpg.Enemy;
import com.isep.rpg.Game;
import com.isep.rpg.Hero;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameController implements Initializable {
    @FXML
    Label playTurn;
    @FXML
    ComboBox<Integer> c_num;
    @FXML
    ComboBox<String> c_target;
    @FXML
    VBox vb_heros, vb_enemy;
    @FXML
    ListView<String> infos;

    @FXML
    Button start, ok;

    Game game = new Game();

    boolean isLastBossCome = false;

    ToggleGroup thero = new ToggleGroup();
    ToggleGroup tenemy = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        c_num.getItems().addAll(1, 2, 3, 4, 5, 6);
        c_num.getSelectionModel().select(0);
        c_target.getItems().addAll("attack", "defense", "cure", "food", "potion");
        c_target.getSelectionModel().select(0);
        c_num.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                infos.getItems().clear();
                game.getHeroes().clear();
                vb_heros.getChildren().clear();
                vb_enemy.getChildren().clear();
                start.setDisable(false);
                thero.getToggles().clear();
                tenemy.getToggles().clear();
            }
        });
    }

    @FXML
    public void addHero(Event event) {
        if (vb_heros.getChildren().size() >= c_num.getSelectionModel().getSelectedItem()) {
            return;
        }
        String type = ((Button) event.getSource()).getText();
        RadioButton radioButton = new RadioButton(type);
        radioButton.setToggleGroup(thero);
        Hero hero = game.getInputParser().addHero(type);
        radioButton.setUserData(hero);
        vb_heros.getChildren().add(radioButton);
        infos.getItems().add("You have chosen: " + type + " participates in the battle. ");
    }

    @FXML
    public void start(Event event) {
        if (thero.getToggles().size() != c_num.getSelectionModel().getSelectedItem()) {
            return;
        }
        game.generateCombat();
        List<Enemy> enemies = game.getEnemies();
        for (Enemy enemy : enemies) {
            //Add enemies to the interface
            RadioButton radioButton = new RadioButton(enemy.getName());
            radioButton.setToggleGroup(tenemy);
            radioButton.setUserData(enemy);
            vb_enemy.getChildren().add(radioButton);
            infos.getItems().add("add enemies：" + enemy.getName());
        }
        infos.getItems().add("Please use your wisdom and courage to start your own journey!!!!");
        infos.getItems().add("START FIGHTING !!!");
        start.setDisable(true);
        infos.scrollTo(Integer.MAX_VALUE);
        thero.selectToggle((Toggle) vb_heros.getChildren().get(0));
        ok.setDisable(false);
        playTurn.setText("Round one");
        game.playGame();

    }

    @FXML
    public void ok(Event event) {
        String selectedItem = c_target.getSelectionModel().getSelectedItem();
        Hero hero;
        if (!start.isDisable()) {
            infos.getItems().add("Please start the game first");
            infos.scrollTo(Integer.MAX_VALUE);
            return;
        }
        infos.getItems().add("The " + game.getPlayerTurn() + " round");
        infos.getItems().add(game.getInputParser().getInfo());

        if (thero.getSelectedToggle() == null) return;
        hero = (Hero) thero.getSelectedToggle().getUserData();
        Enemy enemy = null;
        if (tenemy.getSelectedToggle() != null) {
            enemy = (Enemy) tenemy.getSelectedToggle().getUserData();
        }
        String[] parser = game.getInputParser().parser(hero, selectedItem, enemy);
        if (parser[0].equals("fail")) {
            infos.getItems().add(parser[1]);
            return;
        } else if (parser[0].equals("success")) {
            infos.getItems().add(parser[1]);
        }
        check();
        int winner = game.getWinner();
        if (winner != 0) {
            isLastBossCome = false;
            infos.getItems().add(winner == 1 ? "（hero wins）" : "（enemy wins）");
            infos.scrollTo(Integer.MAX_VALUE);
            ok.setDisable(true);
            return;
        }
        infos.getItems().add("（enemy round）");
        infos.getItems().add(game.getInputParser().enemyAction());
        game.setPlayerTurn(game.getPlayerTurn() + 1);
        playTurn.setText("The " + (game.getPlayerTurn() + 1) + " round");
        check();
        winner = game.getWinner();
        if (winner != 0) {
            isLastBossCome = false;
            infos.getItems().add(winner == 1 ? "（hero wins）" : "（enemy wins）");
            infos.scrollTo(Integer.MAX_VALUE);
            ok.setDisable(true);
            return;
        }

        //After the round is over, determine whether the enemy has been defeated, and if so, increase the hero's attributes
        if (game.getDeadEnemyTurn().size() > 0) {
            //Improve attributes
            Random random = new Random();
            String[] award = new String[]{"increase armor", "increased damage", "Add potions and food"};
            //Clear the enemies that died in the current round
            game.getDeadEnemyTurn().clear();
            String item = award[random.nextInt(award.length)];
            infos.getItems().add("Kill an enemy, the attribute of all members will increase randomly" + item);
            infos.getItems().add(game.getInputParser().awardHero(item));
        }
        infos.scrollTo(Integer.MAX_VALUE);
    }


    //Check if character is dead

    private void check() {
        List<Hero> heroes = game.getHeroes().stream().filter(h -> h.getLifePoints() <= 0).collect(Collectors.toList());
        if (heroes.size() > 0) {
            for (Hero hero : heroes) {
                Node node = vb_heros.getChildren().stream().filter(n -> n.getUserData() == hero).findFirst().get();
                //Grayed out on death, unchecked
                node.setDisable(true);
                ((ToggleButton) node).setSelected(false);
            }
        }
        List<Enemy> enemies = game.getEnemies().stream().filter(e -> e.getLifePoints() <= 0).collect(Collectors.toList());
        if (enemies.size() > 0) {
            for (Enemy enemy : enemies) {
                Node node = vb_enemy.getChildren().stream().filter(e -> e.getUserData() == enemy).findFirst().get();
                //Grayed out on death, unchecked
                node.setDisable(true);
                ((ToggleButton) node).setSelected(false);
            }
        }
        enemies = game.getEnemies().stream().filter(e -> e.getLifePoints() > 0).collect(Collectors.toList());
        if (enemies.size() == 0 && !isLastBossCome) {
            game.getEnemies().clear();
            vb_enemy.getChildren().clear();
            //All minions die, join the boss
            isLastBossCome = true;
            Boss boss = new Boss();
            game.getEnemies().add(boss);
            RadioButton radioButton = new RadioButton(boss.getName());
            radioButton.setToggleGroup(tenemy);
            radioButton.setUserData(boss);
            vb_enemy.getChildren().add(radioButton);
            infos.getItems().add("All normal minions die, join the boss：" + boss.getName() + " SHOW！ " + boss.toString());
        }
    }
}
