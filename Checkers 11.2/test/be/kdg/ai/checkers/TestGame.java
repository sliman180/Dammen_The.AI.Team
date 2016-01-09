package be.kdg.ai.checkers;

import be.kdg.ai.checkers.algorithm.Algorithm;
import be.kdg.ai.checkers.algorithm.MinimaxAlgorithm;
import be.kdg.ai.checkers.command.IMoveCommand;
import be.kdg.ai.checkers.command.MoveCommand;
import be.kdg.ai.checkers.command.NoMoveException;
import be.kdg.ai.checkers.controller.Controller;
import be.kdg.ai.checkers.controller.GuiController;
import be.kdg.ai.checkers.domain.BoardState;
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

    GameManager gameManager;
    Gui swingGui;
    Controller guiController;
    Player me;
    IMoveCommand moveCommand;
    Algorithm miniMaxMock;
    Logger logger;

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


        //autowiren
        guiController.setGui(swingGui);
        guiController.addListener(gameManager);
        gameManager.setController(guiController);
        gameManager.setMoveCommand(moveCommand);
        swingGui.addListeners(guiController);
        moveCommand.addListener(gameManager);
    }

    //early game tests
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

    //mid game tests
    @Test
    public void movePieceShouldChangeBoardState(){
        gameManager.start(me, 8);
        Position myPosition = new Position(5, 0);
        Position myDestination = new Position(4, 1);

        //simulate mouse clicks on gui
        guiController.clicked(myPosition.getRow(), myPosition.getColumn());
        guiController.clicked(myDestination.getRow(), myDestination.getColumn());

        Piece myPosCell = gameManager.getBoardState().getBoard().getPieces()[myPosition.getRow()][myPosition.getColumn()];
        Piece myDestCell = gameManager.getBoardState().getBoard().getPieces()[myDestination.getRow()][myDestination.getColumn()];

        assertEquals("Position 5:0 should be empty.", Piece.EMPTY, myPosCell);
        assertEquals("Position 4:1 should be a white piece.", Piece.WHITE, myDestCell);
    }

    @Test
    public void eatBlackPiece(){
        gameManager.start(me, 8);
        Position myPosition = new Position(5, 4);
        Position myDestination = new Position(4, 3);

        Position enemyPosition = new Position(2, 1);
        Position enemyDestination = new Position(3, 2);
        //setSpecificBoardStateSituation(enemyPosition, enemyDestination);

        //simulate mouse clicks on gui
        guiController.clicked(myPosition.getRow(), myPosition.getColumn());
        guiController.clicked(myDestination.getRow(), myDestination.getColumn());
        gameManager.move(gameManager.getBoardState(), enemyPosition, enemyDestination); //move black

        assertTrue("White should be able to force attack. Size=" + gameManager.getBoardState().getForcedToAttackPieces().size(), gameManager.getBoardState().getForcedToAttackPieces().size() != 0);

        myPosition = myDestination; // 4,3
        myDestination = enemyPosition; // 2,1

        guiController.clicked(myPosition.getRow(), myPosition.getColumn());
        guiController.clicked(myDestination.getRow(), myDestination.getColumn());

        assertEquals("Black should be eaten, black player should have 11 pieces left(board 8x8).", 11, gameManager.getBoardState().getBoard().getBlackLeft());
    }

    @Test //exceptions are caught and logged by Logger.class
    public void checkNotEmptyDestinationMove(){
        gameManager.start(me, 8);
        Position myPosition = new Position(6, 3);
        Position myDestination = new Position(5, 4);

        //simulate mouse clicks on gui
        guiController.clicked(myPosition.getRow(), myPosition.getColumn());
        guiController.clicked(myDestination.getRow(), myDestination.getColumn());

        String logError = "destination is not empty";

        assertTrue("This should generate an error due to improper coordinates", logger.getLastLogMessage().contains(logError));
    }

    @Test //exceptions are caught and logged by Logger.class
    public void checkVerticalHorizontalMove(){
        gameManager.start(me, 8);
        Position myPosition = new Position(5, 4);
        Position myDestination = new Position(3, 4);

        //simulate mouse clicks on gui
        guiController.clicked(myPosition.getRow(), myPosition.getColumn());
        guiController.clicked(myDestination.getRow(), myDestination.getColumn());

        String logError = "vertical";

        assertTrue("This should generate an error due to improper coordinates", logger.getLastLogMessage().contains(logError));
    }

    //todo: werkt nog niet
    //@Test
    public void checkEnemyShouldMoveByHimself(){
        gameManager.start(me, 8);
        Position myPosition = new Position(5, 0);
        Position myDestination = new Position(4, 1);

        //simulate mouse clicks on gui
        guiController.clicked(myPosition.getRow(), myPosition.getColumn());
        guiController.clicked(myDestination.getRow(), myDestination.getColumn());

        //click randomly, 0:0 cannot be clicked
        guiController.clicked(0,1);
        //Position[] enemyPositions = when(miniMaxMock.getBestMove(null, null)).thenReturn(getEnemyMovePositions1());
        Position[] enemyPositions = miniMaxMock.getBestMove(gameManager.getBoardState(), null);

        assertEquals("Position 2:1 should be empty.", Piece.EMPTY, enemyPositions[0]);
        assertEquals("Position 3:0 should be a black piece.", Piece.BLACK, enemyPositions[1]);
    }
    private void setSpecificBoardStateSituation(Position curr, Position dest){
        Piece toBeMovedPiece = gameManager.getBoardState().getBoard().getPieces()[curr.getRow()][curr.getColumn()];
        gameManager.getBoardState().getBoard().getPieces()[dest.getRow()][dest.getColumn()] = toBeMovedPiece;
        gameManager.getBoardState().getBoard().getPieces()[curr.getRow()][curr.getColumn()] = Piece.EMPTY;
    }
    private Position[] getEnemyMovePositions1(){
        Position[] enemyPositions = new Position[2];
        enemyPositions[0] = new Position(2, 1);
        enemyPositions[1] = new Position(3, 0);

        return enemyPositions;
    }

}
