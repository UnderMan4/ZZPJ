package p.lodz.pl.logic.model;

import lombok.Getter;

@Getter
public enum HandValues {
    RoyalFlush("Royal Flush"),
    StraightFlush("Straight Flush"),
    FourOfAKind("Four of a Kind"),
    FullHouse("Full House"),
    Flush("Flush"),
    Straight("Straight"),
    ThreeOfAKind("Three of a Kind"),
    TwoPair("Two Pairs"),
    OnePair("Pair"),
    HighCard("High Card");

    private final String value;

    HandValues(String value) {
        this.value = value;
    }

}
