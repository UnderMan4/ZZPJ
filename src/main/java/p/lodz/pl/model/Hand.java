package p.lodz.pl.model;

import p.lodz.pl.comparators.RankComparator;
import p.lodz.pl.comparators.SuitComparator;
import p.lodz.pl.comparators.SuitRankComparator;

import java.util.Arrays;

import static p.lodz.pl.model.DEFS.FLUSH_SIZE;

public class Hand implements Comparable<Hand> {
    private final SuitRankComparator suitRankComparator = new SuitRankComparator();
    private final SuitComparator suitComparator = new SuitComparator();
    private final RankComparator rankComparator = new RankComparator();
    private final Card[] cards;

    // TODO change to private
    public Boolean royalFlush;
    public Boolean straightFlush;
    Boolean fourOfAKind;
    Boolean fullHouse;
    Boolean flush;
    Boolean straight;
    Boolean threeOfAKind;
    Boolean twoPairs;
    Boolean pair;
    Ranks highCard;



    public Hand(Card[] playerCards, Card[] tableCards) {
        cards = new Card[playerCards.length + tableCards.length];
        System.arraycopy(playerCards, 0, cards, 0, playerCards.length);
        System.arraycopy(tableCards, 0, cards, playerCards.length, tableCards.length);
        Arrays.sort(cards);
    }

    public Card[] getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + Arrays.toString(cards) +
                '}';
    }


    public Ranks highCard() {
        Arrays.sort(cards, rankComparator);
        return cards[0].rank();
    }

    // TODO
    public boolean isPair() {
        return false;
    }

    // TODO
    public boolean isTwoPairs() {
        return false;
    }

    // TODO
    public boolean isThreeOfAKind() {
        return false;
    }

    // TODO
    public boolean isStraight() {
        return false;
    }

    // TODO
    public boolean isFlush() {
        Arrays.sort(cards, suitComparator);

        return false;
    }

    // TODO
    public boolean isFullHouse() {
        return false;
    }

    // TODO
    public boolean isFourOfAKind() {
        return false;
    }

    // TODO
    public boolean isStraightFlush() {
        return false;
    }


    boolean isRoyalFlush() {
        Arrays.sort(cards, suitRankComparator);
        Ranks[] ranks = Ranks.values();
        Suits currentSuit;
        int cardsInRoyalFlush = 0;

        int rankStartIndex = 0;

        // if there is a royal flush, cards will be next to each other after sorting
        for (int i = 0; i < cards.length; i++) {
            // if the card is equal to the highest ranking card (Ace)
            if (cards[i].rank().equals(ranks[0])) {
                currentSuit = cards[i].suit();
                cardsInRoyalFlush = 1;

                // next FLUSH_SIZE-1 cards must be one rank lower with the same suit
                for (int j = 1; j < FLUSH_SIZE; j++) {
                    i++;

                    if ((cards[i].rank().equals(ranks[j]) && cards[i].suit().equals(currentSuit))) {
                        cardsInRoyalFlush++;
                        if (cardsInRoyalFlush == FLUSH_SIZE) {
                            return true;
                        }
                        // if it is the same rank and suit it means there is a duplicate card in flush and card is omitted
                    } else if ((cards[i].rank().equals(ranks[j - 1]) && cards[i].suit().equals(currentSuit))) {
                        j--;
                    } else {
                        break;
                    }
                }

            }
        }
        return false;
    }

    public void evaluate() {
        if (isRoyalFlush()) {
            royalFlush = true;
            straightFlush = true;
            flush = true;
            straight = true;
            highCard = Ranks.values()[0];
        } else {
            royalFlush = false;

        }
        if (isStraightFlush()) {
            straightFlush = true;
            flush = true;
            straight = true;
        } else {
            straightFlush = false;
        }

        // etc...

        highCard = highCard();
    }

    @Override
    public int compareTo(Hand o) {

        int compare = royalFlush.compareTo(o.royalFlush);

        if (compare == 0) {
            compare = straightFlush.compareTo(o.straightFlush);
            //etc ..
        }

        return 0;
    }
}
