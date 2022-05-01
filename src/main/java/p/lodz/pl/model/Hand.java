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


    private boolean evaluated = false;

    // TODO change to private
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


    public boolean isRoyalFlush() {
        return royalFlush;
    }

    public boolean isStraightFlush() {
        return straightFlush;
    }

    public Ranks getHighCard() {
        return highCard;
    }

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


    private void isRoyalFlushOrStraightFlush() {
        Arrays.sort(cards, suitRankComparator);
        Ranks[] ranks = Ranks.values();
        Suits currentSuit;
        Ranks currentRank;
        int cardsInFlush = 0;

        int rankStartIndex = 0;

        // if there is a royal flush, cards will be next to each other after sorting
        for (int i = 0; i < cards.length; i++) {
            currentSuit = cards[i].suit();
            currentRank = cards[i].rank();
            cardsInFlush = 1;

            // next FLUSH_SIZE-1 cards must be one rank lower with the same suit
            for (int j = 1; j < FLUSH_SIZE; j++) {

                if ((i + 1) != cards.length) {
                    i++;
                }


                if ((currentRank.compareTo(cards[i].rank()) == -j) && cards[i].suit().equals(currentSuit)) {
                    cardsInFlush++;
                    if (cardsInFlush == FLUSH_SIZE) {
                        straightFlush = true;
                        if (currentRank.equals(Ranks.values()[0])) {
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
                    if (currentRank == Ranks.values()[Ranks.values().length - FLUSH_SIZE + 1]) {
                        // check if deck contains needed ace
                        if (Arrays.asList(cards).contains(new Card(Ranks.values()[0], currentSuit))) {
                            straightFlush = true;
                            break;
                        }
                    }


                } else {
                    if ((i + 1) != cards.length) {
                        i--;
                    }
                    break;
                }
            }
        }
    }

    public void evaluate() {
        if (!evaluated) {
            isRoyalFlushOrStraightFlush();
            if (!royalFlush || !straightFlush) {
                // TODO
                evaluated = true;
            }


            evaluated = true;
        }
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
                return 50;
            } else {
                return -50;
            }

            // either no straight flush on both hands or straight flush on both hands
        } else if (straightFlush) {
            // in such case there can be more than one straight flush
            // then winner is which flush starts from higher ranked card
            if (this.getHighCard().compareTo(o.getHighCard()) > 0) {
                return 50;
            } else if (this.getHighCard().compareTo(o.getHighCard()) < 0) {
                return -50;
            } else {
                return 0;
            }
        }

        // not yet implemented

        return 0;
    }
}
