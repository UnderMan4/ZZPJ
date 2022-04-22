package p.lodz.pl.model;

import org.junit.jupiter.api.Test;
import p.lodz.pl.exceptions.NoCardsInDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static p.lodz.pl.model.DEFS.DECK_SIZE;

class DeckTest {

    @Test
    void drawTest() throws NoCardsInDeck {
        Deck deck = new Deck();
        int initialDeckSize = 52;
        while (deck.getCardsRemaining() > 0) {

            assertEquals(initialDeckSize, deck.getCardsRemaining());
            deck.draw();
            initialDeckSize--;
        }
        // no more cards remaining
        assertThrows(NoCardsInDeck.class, deck::draw);
    }

    @Test
    void deckSize() {
        Deck deck = new Deck();
        assertEquals(deck.getCardDeck().size(), DECK_SIZE);
    }


}