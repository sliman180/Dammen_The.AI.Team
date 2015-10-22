package be.kdg.ai.dammen.gui;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.piece.Piece;

import java.awt.*;

/**
 * Created by Sliman on 1-10-2015.
 */
public interface Gui {
    void showBoard(Board board);
    void addListeners(GuiListener newListener);
    void changeColor(Piece piece, Color color);
    void notifyListeners();
}
