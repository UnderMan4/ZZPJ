package p.lodz.pl.logic.model;

import p.lodz.pl.logic.exceptions.NoCardsInDeck;

import java.util.ArrayList;
import java.util.Collections;

import static p.lodz.pl.logic.model.DEFS.DECK_SIZE;

public class Deck  {
    private final ArrayList<Card> cardDeck;

    public Deck() {
        cardDeck = new ArrayList<Card>(DECK_SIZE);
        for (Suits suit :
                Suits.values()) {
            for (Ranks rank :
                    Ranks.values()) {
                cardDeck.add(new Card(rank, suit));
            }
        }
        for(int i = 0; i < 3; i++) {
            Collections.shuffle(cardDeck);
        }
    }

    public ArrayList<Card> getCardDeck() {
        return cardDeck;
    }

    public Card draw() throws NoCardsInDeck {
        if (cardDeck.size() == 0) {
            throw new NoCardsInDeck("No more cards in deck");
        }
        Card card = cardDeck.get(cardDeck.size() - 1);
        cardDeck.remove(cardDeck.size() - 1);
        return card;
    }

    public int getCardsRemaining() {
        return cardDeck.size();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cardDeck=" + cardDeck +
                '}' + "\n Size: " + cardDeck.size();
    }
}
