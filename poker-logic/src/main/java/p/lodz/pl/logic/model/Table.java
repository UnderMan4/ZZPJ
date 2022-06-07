package p.lodz.pl.logic.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import p.lodz.pl.logic.exceptions.CannotJoinGame;
import p.lodz.pl.logic.exceptions.NoCardsInDeck;
import p.lodz.pl.logic.exceptions.NotEnoughPlayersException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static p.lodz.pl.logic.model.DEFS.*;

@Log
@Getter
@Setter
public class Table {

    List<Player> playersList = new ArrayList<>();
    List<Card> communityCards = new ArrayList<>(3);

    private int currentBet;
    private int roundPot;
    private int totalPot;
    private Deck deck;

    private int dealerIndex;
    private int smallBlindIndex;
    private int bigBlindIndex;
    private int currentPlayerIndex;



    public void initGame() {
        deck = new Deck();
        currentBet = 0;
        roundPot = 0;
        totalPot = 0;
        dealerIndex = 0;
        smallBlindIndex = 0;
        bigBlindIndex = 0;
        currentPlayerIndex = 0;
        playersList.clear();
    }

    public void joinGame(Player player) throws CannotJoinGame {
        if (playersList.size() < MAX_PLAYERS) {
            if (player.getPlayerChips() > MIN_BET) {
                if(!playersList.contains(player)) {
                    playersList.add(player);
                }
            } else {
                throw new CannotJoinGame("Insufficient chips to join the game");
            }
        } else {
            throw new CannotJoinGame("Maximum amount of players in the game");
        }
    }

    public void startGame() throws NotEnoughPlayersException {
        if (playersList.size() > 1) {
            for (Player player : playersList) {
                try {
                    for (int i = 0; i < 2; i++) {
                        Card card = deck.draw();
                        player.addPlayerCard(card);
                    }
                } catch (NoCardsInDeck e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new NotEnoughPlayersException("Not enough players to start the game");
        }
        Random random = new Random();
        dealerIndex = random.nextInt(playersList.size());

        if  (playersList.size() == 2){
            smallBlindIndex = 0;
            bigBlindIndex = 1;
        } else if (dealerIndex == playersList.size() - 2) {
            smallBlindIndex = playersList.size() - 1;
            bigBlindIndex = 0;
        } else if (dealerIndex == playersList.size() - 1) {
            smallBlindIndex = 0;
            bigBlindIndex = 1;
        } else {
            smallBlindIndex = dealerIndex + 1;
            bigBlindIndex = dealerIndex + 2;
        }

        currentBet = BIG_BLIND;

        log.info("Dealer: " + playersList.get(dealerIndex).getName());
    }


    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public List<String> getPlayersNames() {
        List<String> playersNames = new ArrayList<>();
        for (Player player : playersList) {
            playersNames.add(player.getName());
        }
        return playersNames;
    }

    @Override
    public String toString() {
        return "Table{" +
                "playersList=" + playersList +
                ", currentBet=" + currentBet +
                '}';
    }
}
