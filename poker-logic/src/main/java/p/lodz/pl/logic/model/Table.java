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

    List<Player> playersList = new ArrayList<Player>();
    List<Card> communityCards = new ArrayList<Card>(3);

    private int currentBet;
    private int roundPot;
    private int totalPot;
    private Deck deck;

    private int dealerIndex;
    private int smallBlindIndex;
    private int bigBlindIndex;
    private int currentPlayerIndex;


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


        if (dealerIndex == playersList.size() - 1) {
            smallBlindIndex = 0;
            bigBlindIndex = 1;
        } else if (dealerIndex == playersList.size() - 2) {
            smallBlindIndex = playersList.size() - 1;
            bigBlindIndex = 0;
        } else {
            smallBlindIndex = dealerIndex + 1;
            bigBlindIndex = dealerIndex + 2;
        }

        currentBet = BIG_BLIND;

        log.info("Dealer: " + playersList.get(dealerIndex).getName());
    }

//    public void game() {
//        try {
//            preFlopRound();
//            flopRound();
//            turnRound();
//            riverRound();
//        } catch (Exception e) {
//            log.severe(e.getMessage());
//        }
//    }

//    public void preFlopRound() {
//        Scanner scanner = new Scanner(System.in);
//        currentPlayerIndex = bigBlindIndex != playersList.size() - 1 ? bigBlindIndex + 1 : 0;
//        int playerToActIndex = currentPlayerIndex;
//
//        log.info("**********Pre-flop round**********");
//
//        do {
//            log.info("Current bet: " + currentBet);
//            log.info("Round pot: " + roundPot);
//            log.info("Player to act: " + playersList.get(playerToActIndex).getName());
//
//            if (playersList.get(currentPlayerIndex).isFold()) {
//                System.out.println(playersList.get(currentPlayerIndex).getName() + " is folded");
//                currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//                continue;
//            }
//
//            if(getAllFoldedPlayers() == playersList.size() - 1) {
//                log.info("Player" + currentPlayerIndex + "wins the game!!");
//                return;
//            }
//
//            log.info("It's " + playersList.get(currentPlayerIndex).getName() + "'s turn");
//            String option = scanner.nextLine();
//
//            if (option.equals("fold")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " folded");
//                playersList.get(currentPlayerIndex).fold();
//            }
//            if (option.equals("call")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " called");
//                playersList.get(currentPlayerIndex).setBet(currentBet);
//                playersList.get(currentPlayerIndex).call();
//                roundPot += currentBet;
//            }
//            if (option.equals("raise")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " raised");
//                currentBet += 20;
//                playersList.get(currentPlayerIndex).raise(20);
//                roundPot += currentBet;
//                playerToActIndex = currentPlayerIndex;
//            }
//            currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//
//
//        } while (playerToActIndex != currentPlayerIndex);
//
//        log.info("Pre flop round finished");
//        totalPot += roundPot;
//        roundPot = 0;
//    }
//
//    public void flopRound() throws NoCardsInDeck {
//        Scanner scanner = new Scanner(System.in);
//        currentPlayerIndex = dealerIndex != playersList.size() - 1 ? dealerIndex + 1 : 0;
//        int playerToActIndex = currentPlayerIndex;
//        boolean wasRaised = false;
//
//        communityCards.add(deck.draw());
//        communityCards.add(deck.draw());
//        communityCards.add(deck.draw());
//
//        log.info("**********Flop round**********");
//
//        do {
//            log.info("Community cards: " + communityCards.toString());
//
//            log.info("Current bet: " + currentBet);
//            log.info("Round pot: " + roundPot);
//            log.info("Player to act: " + playersList.get(playerToActIndex).getName());
//
//            if (playersList.get(currentPlayerIndex).isFold()) {
//                System.out.println(playersList.get(currentPlayerIndex).getName() + " is folded");
//                currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//                continue;
//            }
//
//            if(getAllFoldedPlayers() == playersList.size() - 1) {
//                log.info("Player" + currentPlayerIndex + "wins the game!!");
//                return;
//            }
//
//            String option = scanner.nextLine();
//
//            if (option.equals("fold")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " folded");
//                playersList.get(currentPlayerIndex).fold();
//            }
//            if (option.equals("check") && !wasRaised) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " checked");
//            }
//            if (option.equals("call") && wasRaised) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " called");
//                playersList.get(currentPlayerIndex).setBet(currentBet);
//                playersList.get(currentPlayerIndex).call();
//                roundPot += currentBet;
//            }
//            if (option.equals("raise")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " raised");
//                currentBet += 20;
//                playersList.get(currentPlayerIndex).raise(20);
//                roundPot += currentBet;
//                playerToActIndex = currentPlayerIndex;
//                wasRaised = true;
//            }
//            currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//
//        } while (playerToActIndex != currentPlayerIndex);
//    }
//
//    public void turnRound() throws NoCardsInDeck {
//        Scanner scanner = new Scanner(System.in);
//        currentPlayerIndex = dealerIndex != playersList.size() - 1 ? dealerIndex + 1 : 0;
//        int playerToActIndex = currentPlayerIndex;
//        boolean wasRaised = false;
//
//
//        communityCards.add(deck.draw());
//
//        log.info("**********Turn round**********");
//
//        do {
//            log.info("Community cards: " + communityCards.toString());
//
//            log.info("Current bet: " + currentBet);
//            log.info("Round pot: " + roundPot);
//            log.info("Player to act: " + playersList.get(playerToActIndex).getName());
//
//            if (playersList.get(currentPlayerIndex).isFold()) {
//                System.out.println(playersList.get(currentPlayerIndex).getName() + " is folded");
//                currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//                continue;
//            }
//
//            if(getAllFoldedPlayers() == playersList.size() - 1) {
//                log.info("Player" + currentPlayerIndex + "wins the game!!");
//                return;
//            }
//
//            String option = scanner.nextLine();
//
//            if (option.equals("fold")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " folded");
//                playersList.get(currentPlayerIndex).fold();
//            }
//            if (option.equals("check") && !wasRaised) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " checked");
//            }
//            if (option.equals("call") && wasRaised) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " called");
//                playersList.get(currentPlayerIndex).setBet(currentBet);
//                playersList.get(currentPlayerIndex).call();
//                roundPot += currentBet;
//            }
//            if (option.equals("raise")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " raised");
//                currentBet += 20;
//                playersList.get(currentPlayerIndex).raise(20);
//                roundPot += currentBet;
//                playerToActIndex = currentPlayerIndex;
//                wasRaised = true;
//            }
//            currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//
//        } while (playerToActIndex != currentPlayerIndex);
//    }
//
//    public void riverRound() throws NoCardsInDeck {
//        Scanner scanner = new Scanner(System.in);
//        currentPlayerIndex = dealerIndex != playersList.size() - 1 ? dealerIndex + 1 : 0;
//        int playerToActIndex = currentPlayerIndex;
//        boolean wasRaised = false;
//
//
//        communityCards.add(deck.draw());
//
//        log.info("**********River round**********");
//
//        do {
//            log.info("Community cards: " + communityCards.toString());
//
//            log.info("Current bet: " + currentBet);
//            log.info("Round pot: " + roundPot);
//            log.info("Player to act: " + playersList.get(playerToActIndex).getName());
//
//            if (playersList.get(currentPlayerIndex).isFold()) {
//                System.out.println(playersList.get(currentPlayerIndex).getName() + " is folded");
//                currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//                continue;
//            }
//
//            if(getAllFoldedPlayers() == playersList.size() - 1) {
//                log.info("Player" + currentPlayerIndex + " wins the game!!");
//                return;
//            }
//
//            String option = scanner.nextLine();
//
//            if (option.equals("fold")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " folded");
//                playersList.get(currentPlayerIndex).fold();
//            }
//            if (option.equals("check") && !wasRaised) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " checked");
//            }
//            if (option.equals("call") && wasRaised) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " called");
//                playersList.get(currentPlayerIndex).setBet(currentBet);
//                playersList.get(currentPlayerIndex).call();
//                roundPot += currentBet;
//            }
//            if (option.equals("raise")) {
//                log.info(playersList.get(currentPlayerIndex).getName() + " raised");
//                currentBet += 20;
//                playersList.get(currentPlayerIndex).raise(20);
//                roundPot += currentBet;
//                playerToActIndex = currentPlayerIndex;
//                wasRaised = true;
//            }
//            currentPlayerIndex = currentPlayerIndex != playersList.size() - 1 ? currentPlayerIndex + 1 : 0;
//
//        } while (playerToActIndex != currentPlayerIndex);
//
//        for (Player player : playersList) {
//            if(!player.isFold()) {
//                continue;
//            }
//            player.setPlayerHand(new Hand(player.getPlayerCards().toArray(new Card[2]), communityCards.toArray(new Card[5])));
//            player.getPlayerHand().evaluate();
//            System.out.println(player.getName() + ": " + player.getPlayerHand().toString());
//            System.out.println(player.getPlayerHand().checkHand());
//        }
//    }

    public String getCommunityCards() {
        return communityCards.toString();
    }

    @Override
    public String toString() {
        return "Table{" +
                "playersList=" + playersList +
                ", currentBet=" + currentBet +
                '}';
    }
}
