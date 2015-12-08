package be.kdg.ai.checkers.command;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;


/**
 * Created by Sayu on 24/11/2015.
 */
public interface IMoveCommand {
    boolean move(BoardState boardState, Position current, Position destination) throws NoMoveException;
    void addListener(MoveListener listenerToAdd);
}
