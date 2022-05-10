package p.lodz.pl.logic.model;

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


    private boolean evaluated = false;

    private boolean royalFlush;
    private boolean straightFlush;
    private boolean fourOfAKind;
    private boolean fullHouse;
    private boolean flush;
    private boolean straight;
    private boolean threeOfAKind;
    private boolean twoPairs;
    private boolean pair;
    // the highest card in current composition
    // ex. highest card in straight
    private Ranks highCard;


    public Hand(Card[] playerCards, Card[] tableCards) {
        cards = new Card[playerCards.length + tableCards.length];
        System.arraycopy(playerCards, 0, cards, 0, playerCards.length);
        System.arraycopy(tableCards, 0, cards, playerCards.length, tableCards.length);
        Arrays.sort(cards);
    }

    public Hand(Card[]... cardArrays) {
        ArrayList<Card> cardList = new ArrayList<Card>();
        for (Card[] cards :
                cardArrays) {
            Collections.addAll(cardList, cards);
        }
        cards = cardList.toArray(new Card[0]);
    }

    public boolean isRoyalFlush() {
        return royalFlush;
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

    public Ranks getHighCard() {
        return highCard;
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
    private boolean isPair() {
        return false;
    }

    // TODO
    private boolean checkIfTwoPairs() {
        return false;
    }


    // TODO
    private boolean checkIfStraight() {
        return false;
    }

    // TODO
    private boolean checkIfFlush() {
        Arrays.sort(cards, suitComparator);

        return false;
    }

    // TODO
    private boolean checkIfFullHouse() {
        return false;
    }

    // check if hand contains ThreeOfAKind or FourOfAKind
    // both assumptions are checked together for optimization
    private void checkIfThreeOrFourOfAKind() {
        Arrays.sort(cards, rankComparator);
        int cardsInFourOfAKind = 1;
        highCard = cards[0].rank();
        Ranks threeOfAKindRank = highCard;

        for (int i = 1; i < cards.length; i++) {

            if (cards[i].rank().equals(highCard)) {
                cardsInFourOfAKind++;
                if (cardsInFourOfAKind == 3) {
                    threeOfAKind = true;
                    threeOfAKindRank = highCard;
                }
                if (cardsInFourOfAKind == 4) {
                    fourOfAKind = true;
                    return;
                }
            } else {
                cardsInFourOfAKind = 1;
                highCard = cards[i].rank();
            }
        }
        fourOfAKind = false;

        // if hand does not contain four of a kind, saved high card from threeOfAKind is brought back
        if (threeOfAKind) {
            highCard = threeOfAKindRank;
        }

    }


    private void checkIfRoyalFlushOrStraightFlush() {
        Arrays.sort(cards, suitRankComparator);
        Ranks[] ranks = Ranks.values();
        Suits currentSuit;
        int cardsInFlush = 0;

        int rankStartIndex = 0;

        // if there is a royal flush, cards will be next to each other after sorting
        for (int i = 0; i < cards.length; i++) {
            currentSuit = cards[i].suit();
            highCard = cards[i].rank();
            cardsInFlush = 1;

            // next FLUSH_SIZE-1 cards must be one rank lower with the same suit
            for (int j = 1; j < FLUSH_SIZE; j++) {


                // we will be checking next card, so we must check if the current card is not the last one in array
                if ((i + 1) != cards.length) {
                    i++;
                }

                // checking if next card is one rank lower than current one and of the same suit
                if ((highCard.compareTo(cards[i].rank()) == -j) && cards[i].suit().equals(currentSuit)) {
                    cardsInFlush++;
                    if (cardsInFlush == FLUSH_SIZE) {
                        straightFlush = true;
                        if (highCard.equals(ranks[0])) {
                            royalFlush = true;
                        }
                        break;
                    }
                    // if it is the same rank and suit it means there is a duplicate card in flush and card is omitted
                } else if ((cards[i].rank().equals(ranks[j - 1]) && cards[i].suit().equals(currentSuit))) {
                    j--;

                    // (in normal poker) if the last card is ace ex. 5, 4, 3, 2, ace
                    // if the last card in ranks is the last card in flush
                } else if (cardsInFlush == FLUSH_SIZE - 1) {
                    // check if straight is from last cards
                    // (in normal poker) check if the flush starts from 5
                    if (highCard == ranks[ranks.length - FLUSH_SIZE + 1]) {
                        // check if deck contains needed ace
                        if (Arrays.asList(cards).contains(new Card(ranks[0], currentSuit))) {
                            straightFlush = true;
                            break;
                        }
                    }


                } else {
                    // if the straight does not continue, we go back one card
                    if ((i + 1) != cards.length) {
                        i--;
                    }
                    break;
                }
            }
        }
    }

    /**
     * evaluate each possible combination
     * evaluation stops when certain combination is reached
     * ex. if a hand contains straight flush other lower combinations are not calculated
     */
    public void evaluate() {
        if (!evaluated) {
            checkIfRoyalFlushOrStraightFlush();
            if (!royalFlush || !straightFlush) {
                checkIfThreeOrFourOfAKind();
                if (!fourOfAKind) {
                    // more logic
                }

            }


            evaluated = true;
        }
    }

    private int compareHighCards(Hand o) {
        return Integer.compare(this.getHighCard().compareTo(o.getHighCard()), 0);
    }

    @Override
    public int compareTo(Hand o) {
        o.evaluate();
        this.evaluate();

        int compare = 0;

        // checking for royal flush
        if (royalFlush ^ o.isRoyalFlush()) {

            // either this or o is royalFlush
            if (royalFlush) {
                return 100;
            } else {
                return -100;
            }

            // either no royal flush on both hands or royal flush on both hands
            // for possible modifications of this game, ex. more than 1 deck is used
            // in such case there can be more than one royal flush
        } else if (royalFlush) {
            return 0;
        }

        if (straightFlush ^ o.isStraightFlush()) {
            // either this or o is straightFlush
            if (straightFlush) {
                return 1;
            } else {
                return -1;
            }

            // either no straight flush in both hands or straight flush in both hands
        } else if (straightFlush) {
            // in such case there can be more than one straight flush
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
            // in such case there can be more than one four of a kind
        } else if (fourOfAKind) {
            // in such case there can be more than one four of a kind flush
            // then winner is which four of a kind that starts from higher ranked card
            return compareHighCards(o);
        }

        // fullhouse

        // flush

        // straight


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

        return 0;
    }
}
