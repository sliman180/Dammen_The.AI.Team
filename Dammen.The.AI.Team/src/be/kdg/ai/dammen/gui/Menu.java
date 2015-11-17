package be.kdg.ai.dammen.gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * A menu for the GUI based on Swing.
 */
public class Menu {
    private JMenuBar jMenuBar;
    private JMenu game;
    private JMenu help;
    private JFrame jFrame;

    public Menu(JFrame jFrame, ActionListener newGameActionListener, ActionListener aboutActionListener) {
        this.jFrame = jFrame;
        this.jMenuBar = new JMenuBar();

        game = new JMenu("Game");
        help = new JMenu("Help");

        JMenuItem about = new JMenuItem("about");
        about.addActionListener(aboutActionListener);

        //TODO: game settings, board cel number, difficulty, ...
        JMenuItem newGame = new JMenuItem("New game");
        newGame.setMnemonic(KeyEvent.VK_N);
        newGame.addActionListener(newGameActionListener);

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(e -> jFrame.dispose());

        game.add(newGame);
        game.add(exit);
        help.add(about);

        jMenuBar.add(game);
        jMenuBar.add(help);
    }

    public JMenuBar getjMenuBar() {
        return jMenuBar;
    }
}
