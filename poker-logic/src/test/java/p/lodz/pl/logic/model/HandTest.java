package p.lodz.pl.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {


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
    // straight flush 5,4,3,2,1 with player cards
    Card[] straightFlushCommunity1 = new Card[]{
            new Card(Ranks.Two, Suits.Clover),
            new Card(Ranks.King, Suits.Tiles),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.Four, Suits.Clover),
            new Card(Ranks.Three, Suits.Clover)
    };

    // straight flush 5,4,3,2,1
    Card[] straightFlushCommunity2 = new Card[]{
            new Card(Ranks.Ace, Suits.Tiles),
            new Card(Ranks.Two, Suits.Tiles),
            new Card(Ranks.Three, Suits.Tiles),
            new Card(Ranks.Four, Suits.Tiles),
            new Card(Ranks.Five, Suits.Tiles)
    };

    // straight flush 7,6,5,4,3
    Card[] straightFlushCommunity3 = new Card[]{
            new Card(Ranks.Three, Suits.Pikes),
            new Card(Ranks.Four, Suits.Pikes),
            new Card(Ranks.Five, Suits.Pikes),
            new Card(Ranks.Six, Suits.Pikes),
            new Card(Ranks.Seven, Suits.Pikes)
    };


    Card[] fourOfAKindPlayer1 = new Card[]{new Card(Ranks.Two, Suits.Pikes), new Card(Ranks.Five, Suits.Clover)};
    Card[] fourOfAKindPlayer2 = new Card[]{new Card(Ranks.Six, Suits.Pikes), new Card(Ranks.Five, Suits.Hearts)};

    // non-traditional community cards 6 cards on table
    Card[] fourOfAKindCommunity = new Card[]{
            new Card(Ranks.Two, Suits.Tiles),
            new Card(Ranks.Two, Suits.Hearts),
            new Card(Ranks.Two, Suits.Clover),
            new Card(Ranks.Six, Suits.Tiles),
            new Card(Ranks.Six, Suits.Hearts),
            new Card(Ranks.Six, Suits.Clover),
    };


    Card[] threeOfAKindPlayer1 = new Card[]{new Card(Ranks.Two, Suits.Pikes), new Card(Ranks.Nine, Suits.Clover)};
    Card[] threeOfAKindPlayer2 = new Card[]{new Card(Ranks.Six, Suits.Pikes), new Card(Ranks.Eight, Suits.Hearts)};
    Card[] threeOfAKindPlayer3 = new Card[]{new Card(Ranks.Ace, Suits.Pikes), new Card(Ranks.Eight, Suits.Hearts)};

    // non-traditional community cards 6 cards on table
    Card[] threeOfAKindCommunity = new Card[]{
            new Card(Ranks.Two, Suits.Tiles),
            new Card(Ranks.Two, Suits.Hearts),
            new Card(Ranks.Ace, Suits.Clover),
            new Card(Ranks.Ace, Suits.Tiles),
            new Card(Ranks.Six, Suits.Tiles),
            new Card(Ranks.Six, Suits.Hearts),
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
        // hand1 < hand2 < hand3
        Hand hand1 = new Hand(threeOfAKindPlayer1, threeOfAKindCommunity);
        Hand hand2 = new Hand(threeOfAKindPlayer2, threeOfAKindCommunity);
        Hand hand3 = new Hand(threeOfAKindPlayer3, threeOfAKindCommunity);

        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        Collections.sort(hands);

        Assertions.assertEquals(hand3, hands.get(0));

        assertTrue(hand1.compareTo(hand2) > 0);
        assertTrue(hand2.compareTo(hand3) > 0);
        assertTrue(hand2.compareTo(hand1) < 0);
        Assertions.assertTrue(hand1.isThreeOfAKind());
        Assertions.assertTrue(hand2.isThreeOfAKind());
        Assertions.assertTrue(hand3.isThreeOfAKind());
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

        Hand hand1 = new Hand(fourOfAKindPlayer1, fourOfAKindCommunity);
        Hand hand2 = new Hand(fourOfAKindPlayer2, fourOfAKindCommunity);

        hand1.evaluate();
        hand2.evaluate();

        assertTrue(hand1.compareTo(hand2) > 0);
        Assertions.assertTrue(hand1.isFourOfAKind());
        Assertions.assertTrue(hand1.isThreeOfAKind());
        Assertions.assertTrue(hand2.isFourOfAKind());
        Assertions.assertTrue(hand2.isThreeOfAKind());
    }

    @Test
    void compareStraightFlush() {
        // hand1 < hand2 < hand 3
        Hand hand1 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand2 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand3 = new Hand(straightFlushPlayer1, straightFlushCommunity3);
        Hand hand11 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand22 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand33 = new Hand(straightFlushPlayer1, straightFlushCommunity3);


        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        Collections.sort(hands);

        Assertions.assertEquals(hands.get(0), hand3);

        Assertions.assertEquals(0, hand1.compareTo(hand11));
        Assertions.assertEquals(0, hand2.compareTo(hand22));
        Assertions.assertEquals(0, hand3.compareTo(hand33));


        assertTrue(hand1.compareTo(hand3) > 0);
        assertTrue(hand2.compareTo(hand3) > 0);
        Assertions.assertEquals(0, hand1.compareTo(hand2));
    }

    @Test
    void isStraightFlush() {
        Hand hand1 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand2 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand3 = new Hand(straightFlushPlayer1, straightFlushCommunity3);
        Hand hand5 = new Hand(royalFlushPlayer, royalFlushCommunity);
        Hand hand6 = new Hand(royalFlushPlayer2, royalFlushCommunity2);
        Hand hand7 = new Hand(royalFlushPlayer3, royalFlushCommunity3);

        hand1.evaluate();
        hand2.evaluate();
        hand3.evaluate();
        hand5.evaluate();
        hand6.evaluate();
        hand7.evaluate();

        Assertions.assertTrue(hand1.isStraightFlush());
        Assertions.assertTrue(hand2.isStraightFlush());
        Assertions.assertTrue(hand3.isStraightFlush());

        Assertions.assertTrue(hand5.isStraightFlush());
        Assertions.assertTrue(hand6.isStraightFlush());
        Assertions.assertFalse(hand7.isStraightFlush());
    }

    @Test
    void isRoyalFlush() {
        Hand hand1 = new Hand(royalFlushPlayer, royalFlushCommunity);
        Hand hand2 = new Hand(royalFlushPlayer2, royalFlushCommunity2);
        Hand hand3 = new Hand(royalFlushPlayer3, royalFlushCommunity3);

        hand1.evaluate();
        hand2.evaluate();
        hand3.evaluate();

        Assertions.assertTrue(hand1.isRoyalFlush());
        Assertions.assertTrue(hand2.isRoyalFlush());
        Assertions.assertFalse(hand3.isRoyalFlush());


         Assertions.assertEquals(0, hand1.compareTo(hand2));
    }
}