package p.lodz.pl.model;

import org.junit.jupiter.api.Test;

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
            new Card(Ranks.Queen, Suits.Pikes),
            new Card(Ranks.King, Suits.Hearts),
            new Card(Ranks.King, Suits.Clover),
            new Card(Ranks.Ten, Suits.Clover)
    };

    Hand hand = new Hand(royalFlushPlayer, royalFlushCommunity);


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
    void isStraightFlush() {
    }

    @Test
    void isRoyalFlush() {
        Hand hand1 = new Hand(royalFlushPlayer, royalFlushCommunity);
        Hand hand2 = new Hand(royalFlushPlayer2, royalFlushCommunity2);
        System.out.println(hand);

        assertTrue(hand1.isRoyalFlush());
        assertTrue(hand2.isRoyalFlush());


        // assertEquals(0, hand1.compareTo(hand2));
    }
}