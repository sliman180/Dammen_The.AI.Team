package be.kdg.ai.dammen.board;

import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Board {
    private Board[][] board;

    private TypePiece.Status status;


    public Board(TypePiece.Status status) {
        this.status = status;
    }

    public TypePiece.Status getStatus() {
        return status;
    }

    public Board[][] getBoard() {
        return board;
    }
}
