package p.lodz.pl.logic.model;


// can be changed in the future to read from other sources
public interface DEFS {
    final static int PLAYER_HAND_SIZE = 2;
    final static int COMMUNITY_CARDS_COUNT = 5;
    final static int MAX_PLAYERS = 10;
    final static int MIN_BET = 2;
    final static int SMALL_BLIND = 1;
    final static int BIG_BLIND = 2;
    final static int ROUNDS_COUNT = 3;



    // DO NOT CHANGE UNLESS YOU ADD MORE CARDS TO THE GAME
    final static int DECK_SIZE = Suits.values().length * Ranks.values().length;

    // cards needed for standard flush
    final static int FLUSH_SIZE = 5;


}
