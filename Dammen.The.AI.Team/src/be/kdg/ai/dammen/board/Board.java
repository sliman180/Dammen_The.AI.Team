package be.kdg.ai.dammen.board;

import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Board {
    private Piece[][] pieces;
    private int size;
    private int blackLeft;
    private int whiteLeft;


    public Board(Piece[][] pieces,int size) {
        this.pieces = pieces;
        this.size = size;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public int getSize() {
        return size;
    }

    public int getBlackLeft() {
        return blackLeft;
    }

    public int getWhiteLeft() {
        return whiteLeft;
    }

    public void setBlackLeft(int blackLeft) {
        this.blackLeft = blackLeft;
    }

    public void setWhiteLeft(int whiteLeft) {
        this.whiteLeft = whiteLeft;
    }
}
