package p.lodz.pl.logic.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player {

    private String name;
    private int playerChips;
    private int currentBet;
    private Hand playerHand;
    private List<Card> playerCards;

    private boolean isDealer;
    private boolean isSmallBlind;
    private boolean isBigBlind;
    private boolean isFold;

    public Player(String name, int playerChips, int currentBet, Hand playerHand) {
        this.name = name;
        this.playerChips = playerChips;
        this.currentBet = currentBet;
        this.playerHand = playerHand;
        this.playerCards = new ArrayList<>();
    }

    public int getPlayerChips() {
        return playerChips;
    }

    public void fold() {
        isFold = true;
    }

    public void call() {
        playerChips -= currentBet;
    }

    public void raise(int raiseAmount) {
        currentBet += raiseAmount;
        playerChips -= currentBet;
    }

    public int allIn() {
        int totalAmount = playerChips;
        playerChips = 0;
        return totalAmount;
    }

    public void setPlayerChips(int playerChips) {
        this.playerChips = playerChips;
    }

    public int getBet() {
        return currentBet;
    }

    public void setBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public void addPlayerCard(Card newCard) {
        this.playerCards.add(newCard);
    }

    public boolean isDealer() {
        return isDealer;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    public boolean isFold() {
        return isFold;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerChips=" + playerChips +
                ", currentBet=" + currentBet +
                ", playerHand=" + playerHand +
                ", playerCards=" + playerCards +
                '}';
    }
}
