package be.kdg.ai.dammen.piece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Piece {
    private TypePiece.Status status;

    public Piece(TypePiece.Status status) {
        this.status = status;
    }

    public TypePiece.Status getStatus() {
        return status;
    }
}
