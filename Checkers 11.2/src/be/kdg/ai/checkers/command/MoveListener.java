package be.kdg.ai.checkers.command;

import be.kdg.ai.checkers.domain.BoardState;

/**
 * Created by Sayu on 25/11/2015.
 */
public interface MoveListener {
    void updateBoardState(BoardState newBoardState, boolean isTreeFiller);
    void endGame();
}
