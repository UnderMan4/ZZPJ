package p.lodz.pl.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import p.lodz.pl.logic.comparators.RankComparator;
import p.lodz.pl.logic.comparators.SuitComparator;
import p.lodz.pl.logic.comparators.SuitRankComparator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorTest {

    @Test
    public void RankComparator() {
        RankComparator rankComparator = new RankComparator();
        Card card1 = new Card(Ranks.Ace, Suits.Hearts);
        Card card2;

        int difference = 0;

        for (Ranks rank :
                Ranks.values()) {
            card2 = new Card(rank, Suits.Hearts);
            Assertions.assertEquals(difference, rankComparator.compare(card1, card2));
            difference--;
        }
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
        for (Suits suit :
                Suits.values()) {
            for (Ranks ranks :
                    Ranks.values()) {
                Assertions.assertEquals(cards.get(i).suit(), suit);
                Assertions.assertEquals(cards.get(i).rank(), ranks);
                i++;
            }
        }
    }
}
