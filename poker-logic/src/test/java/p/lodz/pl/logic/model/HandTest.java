package p.lodz.pl.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandTest {


    Card[] royalFlushPlayer = new Card[]{new Card(Ranks.Ace, Suits.Clubs), new Card(Ranks.Jack, Suits.Clubs)};
    Card[] royalFlushCommunity = new Card[]{
            new Card(Ranks.Queen, Suits.Clubs),
            new Card(Ranks.King, Suits.Clubs),
            new Card(Ranks.Queen, Suits.Clubs),
            new Card(Ranks.King, Suits.Clubs),
            new Card(Ranks.Ten, Suits.Clubs)
    };

    Card[] royalFlushPlayer2 = new Card[]{new Card(Ranks.Ace, Suits.Clubs), new Card(Ranks.Jack, Suits.Clubs)};
    Card[] royalFlushCommunity2 = new Card[]{
            new Card(Ranks.Queen, Suits.Clubs),
            new Card(Ranks.King, Suits.Diamonds),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.King, Suits.Clubs),
            new Card(Ranks.Ten, Suits.Clubs)
    };

    Card[] royalFlushPlayer3 = new Card[]{new Card(Ranks.Ace, Suits.Clubs), new Card(Ranks.Jack, Suits.Clubs)};
    Card[] royalFlushCommunity3 = new Card[]{
            new Card(Ranks.Queen, Suits.Clubs),
            new Card(Ranks.King, Suits.Diamonds),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.King, Suits.Clubs),
            new Card(Ranks.Nine, Suits.Clubs)
    };

    //----------------------------------------------------------------------------------------------------------------------

    Card[] straightFlushPlayer1 = new Card[]{new Card(Ranks.Ace, Suits.Clubs), new Card(Ranks.Five, Suits.Clubs)};
    // straight flush 5,4,3,2,1 with player cards
    Card[] straightFlushCommunity1 = new Card[]{
            new Card(Ranks.Two, Suits.Clubs),
            new Card(Ranks.King, Suits.Diamonds),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.Four, Suits.Clubs),
            new Card(Ranks.Three, Suits.Clubs)
    };

    // straight flush 5,4,3,2,1
    Card[] straightFlushCommunity2 = new Card[]{
            new Card(Ranks.Ace, Suits.Diamonds),
            new Card(Ranks.Two, Suits.Diamonds),
            new Card(Ranks.Three, Suits.Diamonds),
            new Card(Ranks.Four, Suits.Diamonds),
            new Card(Ranks.Five, Suits.Diamonds)
    };

    // straight flush 6,5,4,3,2
    Card[] straightFlushCommunity3 = new Card[]{
            new Card(Ranks.Six, Suits.Diamonds),
            new Card(Ranks.Two, Suits.Diamonds),
            new Card(Ranks.Three, Suits.Diamonds),
            new Card(Ranks.Four, Suits.Diamonds),
            new Card(Ranks.Five, Suits.Diamonds)
    };

    // straight flush 7,6,5,4,3
    Card[] straightFlushCommunity4 = new Card[]{
            new Card(Ranks.Three, Suits.Spades),
            new Card(Ranks.Four, Suits.Spades),
            new Card(Ranks.Five, Suits.Spades),
            new Card(Ranks.Six, Suits.Spades),
            new Card(Ranks.Seven, Suits.Spades)
    };

    //----------------------------------------------------------------------------------------------------------------------

    Card[] fourOfAKindPlayer1 = new Card[]{new Card(Ranks.Two, Suits.Spades), new Card(Ranks.Five, Suits.Clubs)};
    Card[] fourOfAKindPlayer2 = new Card[]{new Card(Ranks.Six, Suits.Spades), new Card(Ranks.Five, Suits.Hearts)};

    // non-traditional community cards 6 cards on table
    Card[] fourOfAKindCommunity = new Card[]{
            new Card(Ranks.Two, Suits.Diamonds),
            new Card(Ranks.Two, Suits.Hearts),
            new Card(Ranks.Two, Suits.Clubs),
            new Card(Ranks.Six, Suits.Diamonds),
            new Card(Ranks.Six, Suits.Hearts),
            new Card(Ranks.Six, Suits.Clubs),
    };

    //----------------------------------------------------------------------------------------------------------------------

    Card[] threeOfAKindPlayer1 = new Card[]{new Card(Ranks.Two, Suits.Spades), new Card(Ranks.Two, Suits.Clubs)};
    Card[] threeOfAKindPlayer2 = new Card[]{new Card(Ranks.Six, Suits.Spades), new Card(Ranks.Six, Suits.Hearts)};
    Card[] threeOfAKindPlayer3 = new Card[]{new Card(Ranks.Ace, Suits.Spades), new Card(Ranks.Ace, Suits.Hearts)};

    // non-traditional community cards 6 cards on table
    Card[] threeOfAKindCommunity = new Card[]{
            new Card(Ranks.Two, Suits.Diamonds),
            new Card(Ranks.Ace, Suits.Diamonds),
            new Card(Ranks.Six, Suits.Hearts),
    };

    //----------------------------------------------------------------------------------------------------------------------

    // 2,3,5
    Card[] fullHouseCommunity = new Card[]{
            new Card(Ranks.Two, Suits.Spades),
            new Card(Ranks.Three, Suits.Clubs),
            new Card(Ranks.Five, Suits.Clubs)
    };

    // 5,5,4
    Card[] fullHouseTest1 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Five, Suits.Hearts),
            new Card(Ranks.Two, Suits.Clubs)
    };

    // 6,6,6,4
    Card[] fullHouseTest2 = new Card[]{
            new Card(Ranks.Six, Suits.Spades),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Six, Suits.Hearts),
            new Card(Ranks.Three, Suits.Clubs)
    };

    // J,J,J,2
    Card[] fullHouseTest3 = new Card[]{
            new Card(Ranks.Jack, Suits.Spades),
            new Card(Ranks.Jack, Suits.Clubs),
            new Card(Ranks.Jack, Suits.Hearts),
            new Card(Ranks.Two, Suits.Clubs)
    };

    // J,J,J,3
    Card[] fullHouseTest4 = new Card[]{
            new Card(Ranks.Jack, Suits.Spades),
            new Card(Ranks.Jack, Suits.Clubs),
            new Card(Ranks.Jack, Suits.Hearts),
            new Card(Ranks.Three, Suits.Clubs)
    };


    //----------------------------------------------------------------------------------------------------------------------

    Card[] flushTest1 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Queen, Suits.Clubs),
            new Card(Ranks.Ace, Suits.Clubs),
            new Card(Ranks.Two, Suits.Clubs)
    };

    //----------------------------------------------------------------------------------------------------------------------

    // highest straight
    Card[] straightTest4 = new Card[]{
            new Card(Ranks.Ace, Suits.Clubs),
            new Card(Ranks.King, Suits.Clubs),
            new Card(Ranks.Queen, Suits.Diamonds),
            new Card(Ranks.Jack, Suits.Clubs),
            new Card(Ranks.Ten, Suits.Diamonds)
    };

    // straight 10,9,8,7,6
    Card[] straightTest3 = new Card[]{
            new Card(Ranks.Ten, Suits.Spades),
            new Card(Ranks.Nine, Suits.Hearts),
            new Card(Ranks.Eight, Suits.Spades),
            new Card(Ranks.Six, Suits.Spades),
            new Card(Ranks.Seven, Suits.Hearts)
    };

    // straight 7,6,5,4,3
    Card[] straightTest2 = new Card[]{
            new Card(Ranks.Three, Suits.Spades),
            new Card(Ranks.Four, Suits.Hearts),
            new Card(Ranks.Five, Suits.Spades),
            new Card(Ranks.Six, Suits.Spades),
            new Card(Ranks.Seven, Suits.Hearts)
    };

    // straight 5,4,3,2,1
    Card[] straightTest1 = new Card[]{
            new Card(Ranks.Ace, Suits.Diamonds),
            new Card(Ranks.Two, Suits.Hearts),
            new Card(Ranks.Three, Suits.Diamonds),
            new Card(Ranks.Four, Suits.Diamonds),
            new Card(Ranks.Five, Suits.Hearts)
    };

    //----------------------------------------------------------------------------------------------------------------------

    Card[] twoPairTest1 = new Card[]{
            new Card(Ranks.Six, Suits.Diamonds),
            new Card(Ranks.Six, Suits.Hearts),
            new Card(Ranks.Jack, Suits.Diamonds),
            new Card(Ranks.Jack, Suits.Hearts),
            new Card(Ranks.Two, Suits.Diamonds),
            new Card(Ranks.Two, Suits.Diamonds)
    };

    Card[] twoPairTest2 = new Card[]{
            new Card(Ranks.Ace, Suits.Diamonds),
            new Card(Ranks.Ace, Suits.Hearts),
            new Card(Ranks.Two, Suits.Diamonds),
            new Card(Ranks.Two, Suits.Hearts),
            new Card(Ranks.Ten, Suits.Diamonds)
    };

    Card[] twoPairTest3 = new Card[]{
            new Card(Ranks.Ace, Suits.Diamonds),
            new Card(Ranks.Ace, Suits.Hearts),
            new Card(Ranks.Jack, Suits.Diamonds),
            new Card(Ranks.Jack, Suits.Hearts),
            new Card(Ranks.Ten, Suits.Diamonds),
            new Card(Ranks.Ten, Suits.Diamonds)
    };

    //----------------------------------------------------------------------------------------------------------------------

    // Ace
    Card[] highCard3 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Ace, Suits.Clubs),
    };

    // Queen
    Card[] highCard2 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Queen, Suits.Clubs),
    };


    // 7
    Card[] highCard1 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Seven, Suits.Clubs),
    };

    //----------------------------------------------------------------------------------------------------------------------

    // Ace
    Card[] pair3 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Ace, Suits.Clubs),
            new Card(Ranks.Ace, Suits.Hearts),
    };

    // Queen
    Card[] pair2 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Queen, Suits.Clubs),
            new Card(Ranks.Queen, Suits.Hearts),
    };


    // 7
    Card[] pair1 = new Card[]{
            new Card(Ranks.Five, Suits.Clubs),
            new Card(Ranks.Six, Suits.Clubs),
            new Card(Ranks.Seven, Suits.Clubs),
            new Card(Ranks.Seven, Suits.Hearts),
    };

    @Test
    void highCard() {
        // hand1 < hand2 < hand3
        Hand hand1 = new Hand(highCard1);
        Hand hand2 = new Hand(highCard2);
        Hand hand3 = new Hand(highCard3);
        Hand hand33 = new Hand(highCard3);

        hand33.evaluate();
        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        Collections.sort(hands);

        Assertions.assertEquals(hand3, hands.get(2));
        Assertions.assertEquals(hand2, hands.get(1));
        Assertions.assertEquals(hand1, hands.get(0));

        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand2.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        assertEquals(0, hand3.compareTo(hand33));
    }

    @Test
    void isPair() {
        // hand1 < hand2 < hand3
        Hand hand1 = new Hand(pair1);
        Hand hand2 = new Hand(pair2);
        Hand hand3 = new Hand(pair3);
        Hand hand33 = new Hand(pair3);

        hand33.evaluate();
        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        Collections.sort(hands);

        Assertions.assertEquals(hand3, hands.get(2));
        Assertions.assertEquals(hand2, hands.get(1));
        Assertions.assertEquals(hand1, hands.get(0));

        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand2.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        assertTrue(hand1.isPair());
        assertTrue(hand2.isPair());
        assertTrue(hand3.isPair());
        assertTrue(hand33.isPair());
        assertEquals(0, hand3.compareTo(hand33));
    }

    @Test
    void isTwoPairs() {

        // hand1 < hand2 < hand3
        Hand hand1 = new Hand(twoPairTest1);
        Hand hand2 = new Hand(twoPairTest2);
        Hand hand3 = new Hand(twoPairTest3);
        Hand hand33 = new Hand(twoPairTest3);

        hand33.evaluate();
        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        Collections.sort(hands);

        Assertions.assertEquals(hand3, hands.get(2));
        Assertions.assertEquals(hand2, hands.get(1));
        Assertions.assertEquals(hand1, hands.get(0));

        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand2.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        Assertions.assertTrue(hand1.isTwoPairs());
        Assertions.assertTrue(hand2.isTwoPairs());
        Assertions.assertTrue(hand3.isTwoPairs());
        Assertions.assertTrue(hand33.isTwoPairs());
        assertEquals(0, hand3.compareTo(hand33));
    }

    @Test
    void isThreeOfAKind() {
        // hand1 < hand2 < hand3
        Hand hand1 = new Hand(threeOfAKindPlayer1, threeOfAKindCommunity);
        Hand hand2 = new Hand(threeOfAKindPlayer2, threeOfAKindCommunity);
        Hand hand3 = new Hand(threeOfAKindPlayer3, threeOfAKindCommunity);
        Hand hand33 = new Hand(threeOfAKindPlayer3, threeOfAKindCommunity);

        hand33.evaluate();
        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        Collections.sort(hands);

        Assertions.assertEquals(hand3, hands.get(2));
        Assertions.assertEquals(hand2, hands.get(1));
        Assertions.assertEquals(hand1, hands.get(0));

        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand2.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        Assertions.assertTrue(hand1.isThreeOfAKind());
        Assertions.assertTrue(hand2.isThreeOfAKind());
        Assertions.assertTrue(hand3.isThreeOfAKind());
        Assertions.assertTrue(hand33.isThreeOfAKind());
        assertEquals(0, hand3.compareTo(hand33));
    }

    @Test
    void isStraight() {
        // hand1 < hand2 < hand3 < hand4
        Hand hand1 = new Hand(straightTest1);
        Hand hand2 = new Hand(straightTest2);
        Hand hand3 = new Hand(straightTest3);
        Hand hand4 = new Hand(straightTest4);

        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand3);
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand4);
        Collections.sort(hands);

        Assertions.assertEquals(hand4, hands.get(3));
        Assertions.assertEquals(hand3, hands.get(2));
        Assertions.assertEquals(hand2, hands.get(1));
        Assertions.assertEquals(hand1, hands.get(0));

        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand3.compareTo(hand4) < 0);
        assertTrue(hand2.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        assertTrue(hand4.compareTo(hand2) > 0);
        assertTrue(hand4.compareTo(hand1) > 0);
        Assertions.assertTrue(hand1.isStraight());
        Assertions.assertTrue(hand2.isStraight());
        Assertions.assertTrue(hand3.isStraight());
        Assertions.assertTrue(hand4.isStraight());
    }

    @Test
    void isFlush() {
        Hand hand1 = new Hand(threeOfAKindPlayer1, threeOfAKindCommunity);
        Hand hand2 = new Hand(flushTest1);

        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        Collections.sort(hands);

        Assertions.assertEquals(hand2, hands.get(1));
        assertTrue(hand2.compareTo(hand1) > 0);
        Assertions.assertTrue(hand2.isFlush());
    }

    @Test
    void isFullHouse() {
        // hand1 < hand2 < hand3 < hand4
        Hand hand1 = new Hand(fullHouseTest1, fullHouseCommunity);
        Hand hand2 = new Hand(fullHouseTest2, fullHouseCommunity);
        Hand hand3 = new Hand(fullHouseTest3, fullHouseCommunity);
        Hand hand4 = new Hand(fullHouseTest4, fullHouseCommunity);

        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        hands.add(hand4);
        Collections.sort(hands);

        Assertions.assertEquals(hand4, hands.get(3));
        Assertions.assertEquals(hand3, hands.get(2));
        Assertions.assertEquals(hand2, hands.get(1));
        Assertions.assertEquals(hand1, hands.get(0));

        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand3.compareTo(hand4) < 0);
        assertTrue(hand2.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        assertTrue(hand4.compareTo(hand2) > 0);
        assertTrue(hand4.compareTo(hand1) > 0);
        Assertions.assertTrue(hand1.isFullHouse());
        Assertions.assertTrue(hand2.isFullHouse());
        Assertions.assertTrue(hand3.isFullHouse());
        Assertions.assertTrue(hand4.isFullHouse());
    }

    @Test
    void isFourOfAKind() {

        Hand hand1 = new Hand(fourOfAKindPlayer1, fourOfAKindCommunity);
        Hand hand2 = new Hand(fourOfAKindPlayer2, fourOfAKindCommunity);
        Hand hand22 = new Hand(fourOfAKindPlayer2, fourOfAKindCommunity);

        hand22.evaluate();
        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        Collections.sort(hands);

        Assertions.assertEquals(hand2, hands.get(1));

        assertTrue(hand1.compareTo(hand2) < 0);
        Assertions.assertTrue(hand1.isFourOfAKind());
        Assertions.assertTrue(hand2.isFourOfAKind());
        Assertions.assertTrue(hand22.isFourOfAKind());
        Assertions.assertEquals(0, hand22.compareTo(hand2));
    }

    @Test
    void compareStraightFlush() {
        // hand1 = hand2 < hand 3 < hand 4
        Hand hand1 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand2 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        // straight flush 6,5,4,3,2
        Hand hand3 = new Hand(straightFlushPlayer1, straightFlushCommunity3);
        // straight flush 7,6,5,4,3
        Hand hand4 = new Hand(straightFlushPlayer1, straightFlushCommunity4);
        Hand hand11 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand22 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand33 = new Hand(straightFlushPlayer1, straightFlushCommunity3);
        Hand hand44 = new Hand(straightFlushPlayer1, straightFlushCommunity4);


        ArrayList<Hand> hands = new ArrayList<Hand>();
        hands.add(hand1);
        hands.add(hand2);
        hands.add(hand3);
        hands.add(hand4);
        Collections.sort(hands);

        Assertions.assertEquals(hand4, hands.get(3));
        Assertions.assertEquals(hand3, hands.get(2));

        Assertions.assertEquals(0, hand1.compareTo(hand11));
        Assertions.assertEquals(0, hand2.compareTo(hand22));
        Assertions.assertEquals(0, hand3.compareTo(hand33));
        Assertions.assertEquals(0, hand4.compareTo(hand44));


        assertTrue(hand1.compareTo(hand3) < 0);
        assertTrue(hand2.compareTo(hand3) < 0);
        assertTrue(hand3.compareTo(hand4) < 0);
        assertTrue(hand3.compareTo(hand1) > 0);
        assertTrue(hand4.compareTo(hand1) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        assertTrue(hand3.compareTo(hand2) > 0);
        Assertions.assertEquals(0, hand1.compareTo(hand2));
    }

    @Test
    void isStraightFlush() {
        Hand hand1 = new Hand(straightFlushPlayer1, straightFlushCommunity1);
        Hand hand2 = new Hand(straightFlushPlayer1, straightFlushCommunity2);
        Hand hand3 = new Hand(straightFlushPlayer1, straightFlushCommunity4);
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