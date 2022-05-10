package p.lodz.pl.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import p.lodz.pl.logic.exceptions.NoCardsInDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {

    @Test
    void drawTest() throws NoCardsInDeck {
        Deck deck = new Deck();
        int initialDeckSize = 52;
        while (deck.getCardsRemaining() > 0) {

            Assertions.assertEquals(initialDeckSize, deck.getCardsRemaining());
            deck.draw();
            initialDeckSize--;
        }
        // no more cards remaining
        assertThrows(NoCardsInDeck.class, deck::draw);
    }

    @Test
    void deckSize() {
        Deck deck = new Deck();
        Assertions.assertEquals(deck.getCardDeck().size(), DEFS.DECK_SIZE);
    }


}