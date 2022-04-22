package p.lodz.pl.model;

import p.lodz.pl.exceptions.CannotJoinGame;

import java.util.ArrayList;

import static p.lodz.pl.model.DEFS.MAX_PLAYERS;
import static p.lodz.pl.model.DEFS.MIN_BET;

public class Table {

    ArrayList<Player> playersList = new ArrayList<Player>(4);
    private int currentBet;
    private Deck deck;


    public Table() {
        startGame();
    }

    public void startGame() {
        deck = new Deck();
        currentBet = 0;
        playersList.clear();
    }

    public void joinGame(Player player) throws CannotJoinGame {
        if (playersList.size() < MAX_PLAYERS) {
            if (player.getPlayerChips() > MIN_BET) {
                playersList.add(player);
            } else {
                throw new CannotJoinGame("Insufficient chips to join the game");
            }
        } else {
            throw new CannotJoinGame("Maximum amount of players in the game");
        }
    }
}
