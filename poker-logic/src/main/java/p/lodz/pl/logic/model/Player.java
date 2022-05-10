package p.lodz.pl.logic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private int playerChips;
    private int bet;
    private Hand playerHand;

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
}
