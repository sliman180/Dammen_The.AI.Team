package be.kdg.ai.dammen.gui;

/**
 * Created by Sayu on 20/10/2015.
 */
public interface GuiListener {
    void hover(int row, int column);
    void clicked(int row, int column);
    void newGame();
}
