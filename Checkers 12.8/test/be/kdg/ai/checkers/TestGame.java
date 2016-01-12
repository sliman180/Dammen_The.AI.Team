package be.kdg.ai.checkers;

import be.kdg.ai.checkers.algorithm.Algorithm;
import be.kdg.ai.checkers.algorithm.MinimaxAlgorithm;
import be.kdg.ai.checkers.command.IMoveCommand;
import be.kdg.ai.checkers.command.MoveCommand;
import be.kdg.ai.checkers.controller.Controller;
import be.kdg.ai.checkers.controller.GuiController;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.Player;
import be.kdg.ai.checkers.domain.board.BoardSizeException;
import be.kdg.ai.checkers.domain.board.Position;
import be.kdg.ai.checkers.gui.Gui;
import be.kdg.ai.checkers.gui.SwingGui;
import be.kdg.ai.checkers.logger.Logger;
import be.kdg.ai.checkers.manager.GameManager;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Testing
 */
public class TestGame {

    private GameManager gameManager;
    private Gui swingGui;
    private Controller guiController;
    private Player me;
    private IMoveCommand moveCommand;
    private Algorithm miniMaxMock;
    private Logger logger;

    @Before
    public void setup() {
        miniMaxMock = mock(MinimaxAlgorithm.class);
        gameManager = new GameManager();
        swingGui = new SwingGui();
        guiController = new GuiController();
        me = new Player("Me");
        logger = Logger.getInstance();
        moveCommand = new MoveCommand(false);
        //todo: bovenstaande false is onduidelijk, geef naam


        // manual wiring
        guiController.setGui(swingGui);
        guiController.addListener(gameManager);
        gameManager.setController(guiController);
        gameManager.setMoveCommand(moveCommand);
        swingGui.addListeners(guiController);
        moveCommand.addListener(gameManager);
    }

    // early game tests
    @Test(expected = BoardSizeException.class)
    public void createWrongBoardSize() {
        gameManager.start(me, 11);
    }

    @Test
    public void checkEqualAmountOfPiecesBothPlayers(){
        gameManager.start(me, 10);
        assertEquals("There should be 20 pieces present on each side on board 10x10.", 20, gameManager.getBoardState().getBoard().getWhiteLeft());
        assertEquals("Black player and White player should have the same amount of pieces.", gameManager.getBoardState().getBoard().getBlackLeft(), gameManager.getBoardState().getBoard().getWhiteLeft());
    }

    @Test
    public void createNoParameterBoardGame(){
        gameManager.start(me, null);
        assertEquals("Board size should be set on 8x8 by default", 8, gameManager.getBoardState().getBoard().getSize());
        assertEquals("white left should be 12 on 8x8.", 12, gameManager.getBoardState().getBoard().getWhiteLeft());
        assertEquals("black left should be 12 on 8x8.", 12, gameManager.getBoardState().getBoard().getBlackLeft());

    }

}
