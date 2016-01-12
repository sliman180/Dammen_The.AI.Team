package be.kdg.ai.checkers.domain.board;

/**
 * This class represents the position of a piece
 */
public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return row == position.row && column == position.column;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
