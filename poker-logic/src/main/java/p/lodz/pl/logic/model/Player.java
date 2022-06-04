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
    private int bet;
    private Hand playerHand;
    private List<Card> playerCards;

    public Player(String name, int playerChips, int bet, Hand playerHand) {
        this.name = name;
        this.playerChips = playerChips;
        this.bet = bet;
        this.playerHand = playerHand;
        this.playerCards = new ArrayList<>();
    }

    public int getPlayerChips() {
        return playerChips;
    }

    public void setPlayerChips(int playerChips) {
        this.playerChips = playerChips;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
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

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerChips=" + playerChips +
                ", bet=" + bet +
                ", playerHand=" + playerHand +
                ", playerCards=" + playerCards +
                '}';
    }
}
