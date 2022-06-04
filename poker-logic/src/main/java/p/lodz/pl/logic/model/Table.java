package p.lodz.pl.logic.model;

import p.lodz.pl.logic.exceptions.CannotJoinGame;
import p.lodz.pl.logic.exceptions.NoCardsInDeck;

import java.util.ArrayList;

import static p.lodz.pl.logic.model.DEFS.MAX_PLAYERS;
import static p.lodz.pl.logic.model.DEFS.MIN_BET;

public class Table {

    ArrayList<Player> playersList = new ArrayList<Player>(4);
    private int currentBet;
    private Deck deck;


    public Table() {
        initGame();
    }

    public void initGame() {
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
    
    public void startGame() {
        if(playersList.size() > 1) {
            for (Player player: playersList
                 ) {
                try {
                    for (int i = 0; i < 2; i++) {
                        Card card = deck.draw();
                        player.addPlayerCard(card);
                    }
                } catch (NoCardsInDeck e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Table{" +
                "playersList=" + playersList +
                ", currentBet=" + currentBet +
                '}';
    }
}
