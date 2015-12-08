package be.kdg.ai.checkers.algorithm;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;

import java.util.Enumeration;

/**
 * The algortihm that's going to be used to find the best move for the AI.
 */
public interface Algorithm {
    BoardState search(BoardState node, boolean maximasingPlayer);

//    BoardState calculateBestNode(Enumeration nodes);
//
    Position[] getBestMove(BoardState currentBoardState, BoardState node);
}
