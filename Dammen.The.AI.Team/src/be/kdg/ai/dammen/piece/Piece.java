package be.kdg.ai.dammen.piece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Piece {
    private TypePiece.Status status;
    private TypePiece.Rank rank;
    private int column, row;

    public Piece(TypePiece.Status status) {
        this.status = status;
    }

    public TypePiece.Status getStatus() {
        return status;
    }

    public TypePiece.Rank getRank() {
        return rank;
    }

    public void setRank(TypePiece.Rank rank) {
        this.rank = rank;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
