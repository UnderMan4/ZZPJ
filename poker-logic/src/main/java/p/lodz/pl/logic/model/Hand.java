package p.lodz.pl.logic.model;

import org.jetbrains.annotations.NotNull;
import p.lodz.pl.logic.comparators.RankComparator;
import p.lodz.pl.logic.comparators.SuitComparator;
import p.lodz.pl.logic.comparators.SuitRankComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static p.lodz.pl.logic.model.DEFS.FLUSH_SIZE;

public class Hand implements Comparable<Hand> {
    private final SuitRankComparator suitRankComparator = new SuitRankComparator();
    private final SuitComparator suitComparator = new SuitComparator();
    private final RankComparator rankComparator = new RankComparator();
    private final Card[] cards;

    // for flushes
    private final int[] suitCardCount = new int[Suits.values().length];
    // for straights
    private final int[] rankCardCount = new int[Ranks.values().length];

    private boolean evaluated = false;

    private boolean straightFlush;
    private boolean fourOfAKind;
    private boolean fullHouse;
    private boolean flush;
    private boolean straight;
    private boolean threeOfAKind;
    private boolean twoPairs;
    private boolean pair;
    // the highest card in current composition
    // ex. the highest card in straight
    private Ranks highCard;
    private Ranks secondHighCard;

    // ------------------------------------- variables -------------------------------------

    public Hand(Card @NotNull [] playerCards, Card @NotNull [] tableCards) {
        cards = new Card[playerCards.length + tableCards.length];
        System.arraycopy(playerCards, 0, cards, 0, playerCards.length);
        System.arraycopy(tableCards, 0, cards, playerCards.length, tableCards.length);
        Arrays.sort(cards);
    }

    public Hand(Card[] @NotNull ... cardArrays) {
        ArrayList<Card> cardList = new ArrayList<Card>();
        for (Card[] cards :
                cardArrays) {
            Collections.addAll(cardList, cards);
        }
        cards = cardList.toArray(new Card[0]);
    }

    // ------------------------------------- getters -------------------------------------
    public boolean isRoyalFlush() {
        return isStraightFlush() && highCard.equals(Ranks.values()[Ranks.values().length - 1]);
    }

    public boolean isStraightFlush() {
        return straightFlush;
    }

    public boolean isFourOfAKind() {
        return fourOfAKind;
    }

    public boolean isFullHouse() {
        return fullHouse;
    }

    public boolean isFlush() {
        return flush;
    }

    public boolean isStraight() {
        return straight;
    }

    public boolean isThreeOfAKind() {
        return threeOfAKind;
    }

    public boolean isTwoPairs() {
        return twoPairs;
    }

    public boolean isPair() {
        return pair;
    }

    public Ranks getHighCard() {
        return highCard;
    }

    public Ranks getSecondHighCard() {
        return secondHighCard;
    }

    public Card[] getCards() {
        return cards;
    }


    // ------------------------------------- other functions -------------------------------------
    @Override
    public String toString() {
        return "\n\nHand{" +
                "cards=" + Arrays.toString(cards) +
                '}';
    }

    private void countCards() {
        for (Card card : cards) {
            suitCardCount[card.suit().ordinal()]++;
            rankCardCount[card.rank().ordinal()]++;
        }
    }


    private int compareHighCards(Hand o) {
        return Integer.compare(this.getHighCard().ordinal(), o.getHighCard().ordinal());
    }

    private int compareSecondaryHighCards(Hand o) {
        return Integer.compare(this.getSecondHighCard().ordinal(), o.getSecondHighCard().ordinal());
    }


    @Override
    public int compareTo(@NotNull Hand o) {
        o.evaluate();
        this.evaluate();

        if (straightFlush ^ o.isStraightFlush()) {
            // either this or o is straightFlush
            if (straightFlush) {
                return 1;
            } else {
                return -1;
            }

            // either no straight flush in both hands or straight flush in both hands
        } else if (straightFlush) {
            // straight flush in both hands
            // then winner is which flush starts from higher ranked card
            return compareHighCards(o);
        }

        // checking for four of a kind
        if (fourOfAKind ^ o.isFourOfAKind()) {

            // either this or o is fourOfAKind
            if (fourOfAKind) {
                return 1;
            } else {
                return -1;
            }
            // either no four of a kind in both hands or four of a kind in both hands
        } else if (fourOfAKind) {
            // four of a kind in both hands
            // then winner is which four of a kind that starts from higher ranked card
            return compareHighCards(o);
        }

        // checking for full-house
        if (fullHouse ^ o.isFullHouse()) {

            // either this or o is full-houses
            if (fullHouse) {
                return 1;
            } else {
                return -1;
            }
            // either no full-houses in both hands or full-houses in both hands
        } else if (fullHouse) {
            // two full-houses
            // then winner is full-house that starts from higher ranked card
            if (compareHighCards(o) == 0) {
                // if high cards are equal, lower cards are checked
                return compareSecondaryHighCards(o);
            }
            // if high cards are not equal, winner is high card
            return compareHighCards(o);
        }

        // checking for flush
        if (flush ^ o.isFlush()) {

            // either this or o is flush
            if (flush) {
                return 1;
            } else {
                return -1;
            }
            // either no flush in both hands or flush in both hands
        } else if (flush) {
            // two flushes in hands
            // then winner is which flush that starts from higher ranked card
            return compareHighCards(o);
        }

        // checking for straight
        if (straight ^ o.isStraight()) {

            // either this or o is straight
            if (straight) {
                return 1;
            } else {
                return -1;
            }
            // either no straight in both hands or straight in both hands
        } else if (straight) {
            // two straight in hands
            // then winner is which straight that starts from higher ranked card
            return compareHighCards(o);
        }


        // checking for three of a kind
        if (threeOfAKind ^ o.isThreeOfAKind()) {

            // either this or o is fourOfAKind
            if (threeOfAKind) {
                return 1;
            } else {
                return -1;
            }
            // either no four of a kind in both hands or four of a kind in both hands
            // in such case there can be more than one four of a kind
        } else if (threeOfAKind) {
            // in such case there can be more than one four of a kind flush
            // then winner is which four of a kind that starts from higher ranked card
            return compareHighCards(o);
        }

        // checking for two pairs
        if (twoPairs ^ o.isTwoPairs()) {

            // either this or o is full-houses
            if (twoPairs) {
                return 1;
            } else {
                return -1;
            }
            // either no twoPairs in both hands or twoPairs in both hands
        } else if (twoPairs) {
            // two twoPairs
            // then winner is twoPairs that starts from higher ranked card
            if (compareHighCards(o) == 0) {
                // if high cards are equal, lower cards are checked
                return compareSecondaryHighCards(o);
            }
            // if high cards are not equal, winner is high card
            return compareHighCards(o);
        }

        // checking for pair
        if (pair ^ o.isPair()) {

            // either this or o is full-houses
            if (pair) {
                return 1;
            } else {
                return -1;
            }
            // either no twoPairs in both hands or twoPairs in both hands
        } else if (pair) {
            // two twoPairs
            // then winner is pair with higher card
            return compareHighCards(o);
        }

        // if no combinations, just compare high cards
        return compareHighCards(o);
    }

    // ------------------------------------- checking card combinations -------------------------------------

    public Ranks highCard() {
        Arrays.sort(cards, rankComparator);
        return cards[0].rank();
    }


    private void checkIfStraight() {
        Arrays.sort(cards, rankComparator);
        int straightCount = 1;
        for (int i = 1; i < cards.length; i++) {
            if (straightCount == 1) {
                highCard = cards[i - 1].rank();
            }
            if (highCard.ordinal() - cards[i].rank().ordinal() == straightCount) {
                straightCount++;
            } else {

                // if not duplicate, reset count
                if (cards[i].rank().ordinal() != cards[i - 1].rank().ordinal()) {
                    straightCount = 1;
                }
            }
            if (straightCount == FLUSH_SIZE) {
                straight = true;
                return;
            }
            // only for 5,4,3,2 ace
            if (straightCount == FLUSH_SIZE - 1) {
                if (highCard == Ranks.values()[FLUSH_SIZE - 2]) {
                    if (Arrays.stream(cards).anyMatch(c -> c.rank() == Ranks.values()[Ranks.values().length - 1])) {
                        straight = true;
                        return;
                    }
                }
            }
        }
    }


    private void checkIfFlush() {
        for (int i = 0; i < Suits.values().length; i++) {
            if (suitCardCount[i] >= FLUSH_SIZE) {
                highCard = Ranks.values()[i];
                flush = true;
                return;
            }
        }
    }


    /**
     * Checks for all possible pairs
     */
    private void countPairs() {

        // check for four of a kind
        for (int i = 0; i < rankCardCount.length; i++) {
            if (rankCardCount[i] == 4) {
                highCard = Ranks.values()[i];
                fourOfAKind = true;
                return;
            }
        }

        for (int i = 0; i < rankCardCount.length; i++) {
            if (rankCardCount[i] == 3) {
                highCard = Ranks.values()[i];
                threeOfAKind = true;
                break;
            }
        }

        for (int i = 0; i < rankCardCount.length; i++) {
            if (rankCardCount[i] == 2) {
                if (threeOfAKind) {
                    // if we have a three we save the second-high card for full-house
                    secondHighCard = Ranks.values()[i];
                    fullHouse = true;
                    break;
                }

                if (pair) {
                    twoPairs = true;
                    secondHighCard = highCard;
                } else {
                    pair = true;
                }
                // if not threeOfAKind, that means we can only have pairs here
                // and because we are going from the lowest rank to the highest if a pair is hit then we save the high card
                // if we have two pairs, the second pair rank is higher than current highCard rank, so we set current highCard to secondHighCard
                highCard = Ranks.values()[i];
            }
        }
    }

    private void checkIfStraightFlush() {
        checkIfFlush();
        checkIfStraight();
        if (!(flush && straight)) {
            straightFlush = false;
            return;
        }
        Arrays.sort(cards, suitRankComparator);
        Suits suit;
        for (int i = 0; i < Suits.values().length; i++) {
            if (suitCardCount[i] >= FLUSH_SIZE) {
                suit = Suits.values()[i];
                int straightCount = 1;
                for (int j = 1; j < cards.length; j++) {

                    // if not desired suit skip
                    if (cards[j].suit() != suit) {
                        continue;
                    }
                    if (straightCount == 1) {
                        highCard = cards[j - 1].rank();
                    }
                    if (highCard.ordinal() - cards[j].rank().ordinal() == straightCount) {
                        straightCount++;
                    } else {

                        // if not duplicate, reset count
                        if (cards[j].rank().ordinal() != cards[j - 1].rank().ordinal()) {
                            straightCount = 1;
                        }
                    }
                    if (straightCount == FLUSH_SIZE) {
                        straightFlush = true;
                        return;
                    }
                    // only for 5,4,3,2 ace
                    if (straightCount == FLUSH_SIZE - 1) {
                        if (highCard == Ranks.values()[FLUSH_SIZE - 2]) {
                            Card card = new Card(Ranks.values()[Ranks.values().length - 1], suit);
                            if (Arrays.asList(cards).contains(card)) {
                                straightFlush = true;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * evaluate each possible combination
     * evaluation stops when certain combination is reached
     * ex. if a hand contains straight flush other lower combinations are not checked
     */
    public void evaluate() {
        if (!evaluated) {
            countCards();
            checkIfStraightFlush();
            if (straightFlush) {
                evaluated = true;
                return;
            }

            countPairs();
            if (fourOfAKind || fullHouse) {
                evaluated = true;
                return;
            }

//            checkIfFlush();
            if (flush) {
                evaluated = true;
                return;
            }

//            checkIfStraight();
            if (straight) {
                evaluated = true;
                return;
            }


            if (!(pair || twoPairs || threeOfAKind)) {
                highCard = highCard();
            }
            evaluated = true;
        }
    }

    public String checkHand() {
        evaluate();

        if(isRoyalFlush()) {
            return "Royal Flush";
        }
        if (straightFlush) {
            return "Straight Flush";
        }
        if (fourOfAKind) {
            return "Four of a Kind";
        }
        if (fullHouse) {
            return "Full House";
        }
        if (flush) {
            return "Flush";
        }
        if (straight) {
            return "Straight";
        }
        if (threeOfAKind) {
            return "Three of a Kind";
        }
        if (twoPairs) {
            return "Two Pairs";
        }
        if (pair) {
            return "Pair";
        }
        return "High Card";
    }
}
