package p.lodz.pl.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import p.lodz.pl.logic.comparators.RankComparator;
import p.lodz.pl.logic.comparators.SuitComparator;
import p.lodz.pl.logic.comparators.SuitRankComparator;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComparatorTest {

    @Test
    public void RankComparator() {
        RankComparator rankComparator = new RankComparator();
        Card card1 = new Card(Ranks.Ace, Suits.Hearts);
        Card card2;

        int difference = -12;

        for (Ranks rank :
                Ranks.values()) {
            card2 = new Card(rank, Suits.Hearts);
            Assertions.assertEquals(difference, rankComparator.compare(card1, card2));
            difference++;
        }
    }

    @Test
    public void RankComparatorSort() {
        RankComparator rankComparator = new RankComparator();
        Ranks[] ranks = Ranks.values();
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Ranks rank : ranks) {
            cards.add(new Card(rank, Suits.Hearts));
        }
        cards.sort(rankComparator);
        for (int i = 0; i < ranks.length; i++) {
            assertEquals(ranks[ranks.length -1 -i], cards.get(i).rank());
        }
    }

    @Test
    public void compareCards() {
        Card card1 = new Card(Ranks.Ace, Suits.Hearts);
        Card card2 = new Card(Ranks.Six, Suits.Hearts);
    }

    @Test
    void SuitComparator() {
        SuitComparator suitComparator = new SuitComparator();
        Card card1 = new Card(Ranks.Ace, Suits.Hearts);
        Card card2;

        int difference = 0;

        for (Suits suit :
                Suits.values()) {
            card2 = new Card(Ranks.Ace, suit);
            Assertions.assertEquals(difference, suitComparator.compare(card1, card2));
            difference--;
        }
    }

    @Test
    void SuitRankComparator() {
        int i = 0;
        Deck deck = new Deck();
        SuitRankComparator suitRankComparator = new SuitRankComparator();
        ArrayList<Card> cards = deck.getCardDeck();
        cards.sort(suitRankComparator);
        Ranks[] ranks = Ranks.values();

        for (Suits suit :
                Suits.values()) {
            for (int j = 0; j < ranks.length; j++) {
                Assertions.assertEquals(cards.get(i).suit(), suit);
                Assertions.assertEquals(cards.get(i).rank(), ranks[ranks.length - j - 1]);
                i++;
            }
        }
    }
}
