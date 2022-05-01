package p.lodz.pl.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandTest {



    Card[] playerCards1 = new Card[]{new Card(Ranks.Two, Suits.Hearts), new Card(Ranks.Three, Suits.Hearts)};
    Card[] playerCards2 = new Card[]{new Card(Ranks.King, Suits.Hearts), new Card(Ranks.Ace, Suits.Hearts)};
    Card[] communityCards1 = new Card[]{
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.King, Suits.Clover),
            new Card(Ranks.Queen, Suits.Pikes),
            new Card(Ranks.King, Suits.Tiles),
            new Card(Ranks.Ace, Suits.Hearts)
    };


    Card[] royalFlushPlayer = new Card[]{new Card(Ranks.Ace, Suits.Clover), new Card(Ranks.Jack, Suits.Clover)};
    Card[] royalFlushCommunity = new Card[]{
            new Card(Ranks.Queen, Suits.Clover),
            new Card(Ranks.King, Suits.Clover),
            new Card(Ranks.Queen, Suits.Clover),
            new Card(Ranks.King, Suits.Clover),
            new Card(Ranks.Ten, Suits.Clover)
    };

    Card[] royalFlushPlayer2 = new Card[]{new Card(Ranks.Ace, Suits.Clover), new Card(Ranks.Jack, Suits.Clover)};
    Card[] royalFlushCommunity2 = new Card[]{
            new Card(Ranks.Queen, Suits.Clover),
            new Card(Ranks.King, Suits.Tiles),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.King, Suits.Clover),
            new Card(Ranks.Ten, Suits.Clover)
    };

    Card[] royalFlushPlayer3 = new Card[]{new Card(Ranks.Ace, Suits.Clover), new Card(Ranks.Jack, Suits.Clover)};
    Card[] royalFlushCommunity3 = new Card[]{
            new Card(Ranks.Queen, Suits.Clover),
            new Card(Ranks.King, Suits.Tiles),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.King, Suits.Clover),
            new Card(Ranks.Nine, Suits.Clover)
    };

    Card[] straightFlushPlayer1 = new Card[]{new Card(Ranks.Ace, Suits.Clover), new Card(Ranks.Five, Suits.Clover)};
    Card[] straightFlushCommunity1 = new Card[]{
            new Card(Ranks.Two, Suits.Clover),
            new Card(Ranks.King, Suits.Tiles),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.Four, Suits.Clover),
            new Card(Ranks.Three, Suits.Clover)
    };

    Card[] straightFlushCommunity2 = new Card[]{
            new Card(Ranks.Five, Suits.Clover),
            new Card(Ranks.King, Suits.Tiles),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.Four, Suits.Clover),
            new Card(Ranks.Three, Suits.Clover)
    };

    Card[] straightFlushCommunity3 = new Card[]{
            new Card(Ranks.Two, Suits.Tiles),
            new Card(Ranks.Three, Suits.Tiles),
            new Card(Ranks.Four, Suits.Tiles),
            new Card(Ranks.Five, Suits.Tiles),
            new Card(Ranks.Six, Suits.Tiles)
    };

    Card[] straightFlushCommunity4 = new Card[]{
            new Card(Ranks.Three, Suits.Pikes),
            new Card(Ranks.Four, Suits.Pikes),
            new Card(Ranks.Five, Suits.Pikes),
            new Card(Ranks.Six, Suits.Pikes),
            new Card(Ranks.Seven, Suits.Pikes)
    };

    @Test
    void highCard() {
    }

    @Test
    void isPair() {
    }

    @Test
    void isTwoPairs() {
    }

    @Test
    void isThreeOfAKind() {
    }

    @Test
    void isStraight() {
    }

    @Test
    void isFlush() {
    }

    @Test
    void isFullHouse() {
    }

    @Test
    void isFourOfAKind() {
    }

    @Test
    void compareStraightFlush() {
        Hand hand2 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand4 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand3 = new Hand(straightFlushPlayer1, straightFlushCommunity3);
        Hand hand5 = new Hand(straightFlushPlayer1, straightFlushCommunity3);

        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand2.compareTo(hand4) == 0);
        assertTrue(hand3.compareTo(hand5) == 0);
    }

    @Test
    void isStraightFlush() {
        Hand hand1 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand2 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand3 = new Hand(straightFlushPlayer1, straightFlushCommunity3);
        Hand hand4 = new Hand(straightFlushPlayer1, straightFlushCommunity4);
        Hand hand5 = new Hand(royalFlushPlayer, royalFlushCommunity);
        Hand hand6 = new Hand(royalFlushPlayer2, royalFlushCommunity2);
        Hand hand7 = new Hand(royalFlushPlayer3, royalFlushCommunity3);

        hand1.evaluate();
        hand2.evaluate();
        hand3.evaluate();
        hand4.evaluate();
        hand5.evaluate();
        hand6.evaluate();
        hand7.evaluate();

        assertTrue(hand1.isStraightFlush());
        assertFalse(hand2.isStraightFlush());
        assertTrue(hand3.isStraightFlush());
        assertTrue(hand4.isStraightFlush());
        assertTrue(hand5.isStraightFlush());
        assertTrue(hand6.isStraightFlush());
        assertFalse(hand7.isStraightFlush());
    }

    @Test
    void isRoyalFlush() {
        Hand hand1 = new Hand(royalFlushPlayer, royalFlushCommunity);
        Hand hand2 = new Hand(royalFlushPlayer2, royalFlushCommunity2);
        Hand hand3 = new Hand(royalFlushPlayer3, royalFlushCommunity3);

        hand1.evaluate();
        hand2.evaluate();
        hand3.evaluate();

        assertTrue(hand1.isRoyalFlush());
        assertTrue(hand2.isRoyalFlush());
        assertFalse(hand3.isRoyalFlush());


        // assertEquals(0, hand1.compareTo(hand2));
    }
}