package be.kdg.ai.checkers.algorithm;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;

/**
 * The algortihm that's going to be used to find the best move for the AI.
 */
public interface Algorithm {
    BoardState search(BoardState node, boolean maximasingPlayer);

    Position[] getBestMove(BoardState currentBoardState, BoardState node);
}
