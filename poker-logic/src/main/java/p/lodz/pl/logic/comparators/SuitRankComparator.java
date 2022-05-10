package p.lodz.pl.logic.comparators;

import p.lodz.pl.logic.model.Card;

import java.util.Comparator;

public class SuitRankComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2) {
        SuitComparator suitComparator = new SuitComparator();
        RankComparator rankComparator = new RankComparator();
        int compare = suitComparator.compare(o1, o2);
        if (compare == 0) {
            return rankComparator.compare(o1, o2);
        } else {
            return compare;
        }
    }
}
