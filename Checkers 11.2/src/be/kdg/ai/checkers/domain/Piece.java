package be.kdg.ai.checkers.domain;

import java.io.Serializable;

/**
 * This enum represents a piece in the game.
 */
public enum Piece{
    EMPTY, EMPTY_UNAVAILABLE, WHITE, WHITE_KING, BLACK, BLACK_KING;

    @Override
    public String toString(){
        switch (this){
            case EMPTY: return " ";
            case EMPTY_UNAVAILABLE: return "-";
            case WHITE: return "O";
            case WHITE_KING: return "White king";
            case BLACK: return "X";
            case BLACK_KING: return "Black king";
            default: return "";
        }
    }
}
