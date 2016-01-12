package be.kdg.ai.checkers.command;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;


/**
 * Controls the movement of pieces
 */
public interface IMoveCommand {
    boolean move(BoardState boardState, Position current, Position destination) throws NoMoveException;
    void addListener(MoveListener listenerToAdd);
}
