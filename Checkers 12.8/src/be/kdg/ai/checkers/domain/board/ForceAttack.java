package be.kdg.ai.checkers.domain.board;

/**
 * This class holds two Position-objects which refer to the position of the selected piece (currentPosition) and the destination cell (destinationPosition)
 */
public class ForceAttack {
    private final Position currentPosition;
    private final Position destinationPosition;

    public ForceAttack(Position currentPosition, Position destinationPosition) {
        this.currentPosition = currentPosition;
        this.destinationPosition = destinationPosition;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForceAttack that = (ForceAttack) o;

        if (currentPosition != null ? !currentPosition.equals(that.currentPosition) : that.currentPosition != null)
            return false;
        return !(destinationPosition != null ? !destinationPosition.equals(that.destinationPosition) : that.destinationPosition != null);

    }

    @Override
    public int hashCode() {
        int result = currentPosition != null ? currentPosition.hashCode() : 0;
        result = 31 * result + (destinationPosition != null ? destinationPosition.hashCode() : 0);
        return result;
    }
}
