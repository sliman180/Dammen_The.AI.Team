package be.kdg.ai.checkers.command;

import be.kdg.ai.checkers.domain.BoardState;

/**
 * Listens to moves made by player
 */
public interface MoveListener {
    void updateBoardState(BoardState newBoardState, boolean isTreeFiller);
    void endGame();
}
