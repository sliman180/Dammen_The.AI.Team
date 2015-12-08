package be.kdg.ai.checkers.gui;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;

import java.awt.*;

/**
 * This is the interface for console and swing-gui.
 */
public interface Gui {
    void showBoard(BoardState boardState);

    void changeColor(Position position, Color color);

    void addListeners(GuiListener newListener);

    void showMessage(String gameWonMessage);
}
