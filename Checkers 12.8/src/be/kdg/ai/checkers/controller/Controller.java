package be.kdg.ai.checkers.controller;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.gui.Gui;
import be.kdg.ai.checkers.gui.GuiListener;

/**
 * Controls input and output
 */
public interface Controller extends GuiListener {
    void sendBoardStateToGui(BoardState boardState);
    void addListener(ControllerListener listenerToAdd);
    void setGui(Gui gui);

    void gameWon(String player);
}
