package p.lodz.pl;

import p.lodz.pl.exceptions.NoCardsInDeck;
import p.lodz.pl.model.Deck;

public class ZZPJ {

    public static void main(String[] args) {
        System.out.println("Hello world");
        Deck deck = new Deck();
        System.out.println(deck);
        try {
            System.out.println(deck.draw());
        } catch (NoCardsInDeck e) {
            e.printStackTrace();
        }
        System.out.println(deck);
    }
}
