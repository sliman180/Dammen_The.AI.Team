package be.kdg.ai.checkers.controller;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;

/**
 * This is the interface between GuiController and GameManager. GameManager will be listening to GuiController.
 */
public interface ControllerListener {
    boolean move(BoardState boardState, Position current, Position destination);
}
