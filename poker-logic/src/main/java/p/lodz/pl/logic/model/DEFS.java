package p.lodz.pl.logic.model;


// can be changed in the future to read from other sources
public interface DEFS {
    int PLAYER_HAND_SIZE = 2;
    int COMMUNITY_CARDS_COUNT = 5;
    int MAX_PLAYERS = 2;
    int MIN_BET = 2;
    int SMALL_BLIND = 1;
    int BIG_BLIND = 2;


    // DO NOT CHANGE UNLESS YOU ADD MORE CARDS TO THE GAME
    int DECK_SIZE = Suits.values().length * Ranks.values().length;

    // cards needed for standard flush
    int FLUSH_SIZE = 5;


}
