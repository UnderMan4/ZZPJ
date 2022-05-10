package p.lodz.pl.logic.comparators;

import p.lodz.pl.logic.model.Card;

import java.util.Comparator;

public class SuitComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2) {
        return o1.suit().compareTo(o2.suit());
    }
}
