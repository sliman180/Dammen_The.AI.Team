package be.kdg.ai.checkers.domain.board;

import be.kdg.ai.checkers.domain.Piece;

import java.util.Arrays;

/**
 * This class represents a checkers board.
 */
public class Board {
    private final Piece[][] pieces;
    private final int size;

    public Board(Piece[][] pieces) {
        this.pieces = pieces;
        this.size = pieces.length; //width = height, square
    }


    public Piece[][] getPieces() {
        return pieces;
    }
    public int getBlackLeft(){
        int blackLeft = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (pieces[i][j] == Piece.BLACK || pieces[i][j] == Piece.BLACK_KING)
                    blackLeft++;
            }
        }
        return blackLeft;
    }
    public int getWhiteLeft(){
        int whiteLeft = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (pieces[i][j] == Piece.WHITE || pieces[i][j] == Piece.WHITE_KING)
                    whiteLeft++;
            }
        }
        return whiteLeft;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return getSize() == board.getSize() && Arrays.deepEquals(getPieces(), board.getPieces());

    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(getPieces());
        result = 31 * result + getSize();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int row = 0; row < size; row++) {
            stringBuilder.append(row).append(" [");
            for (int col = 0; col < size; col++) {
                stringBuilder.append(pieces[row][col]);
                stringBuilder.append("|");
            }
            stringBuilder.append("]\n");
        }

        return stringBuilder.toString();
    }
}
