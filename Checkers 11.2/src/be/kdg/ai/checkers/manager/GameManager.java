package be.kdg.ai.checkers.manager;

import be.kdg.ai.checkers.algorithm.Algorithm;
import be.kdg.ai.checkers.algorithm.MinimaxTreeFiller;
import be.kdg.ai.checkers.algorithm.TreeFiller;
import be.kdg.ai.checkers.command.IMoveCommand;
import be.kdg.ai.checkers.command.MoveCommand;
import be.kdg.ai.checkers.command.MoveListener;
import be.kdg.ai.checkers.command.NoMoveException;
import be.kdg.ai.checkers.controller.Controller;
import be.kdg.ai.checkers.controller.ControllerListener;
import be.kdg.ai.checkers.domain.*;
import be.kdg.ai.checkers.domain.board.Board;
import be.kdg.ai.checkers.domain.board.BoardSizeException;
import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.board.Position;
import be.kdg.ai.checkers.logger.Logger;

/**
 * This manager-class dervies most of its methods to others to separate workload.
 */
public class GameManager implements Manager, ControllerListener, MoveListener {
    private IMoveCommand moveCommand;
    private Controller controller;
    private Algorithm algorithm;

    private boolean okayToShow = true;
    private static final int DEFAULT_SIZE = 8;
    private int maxDepthForAI;

    private BoardState boardState;
    private static final Logger logger = Logger.getInstance();


    public void toggleShow() {
        okayToShow = !okayToShow;
    }

    //interface method implementation
    @Override
    public void start(Player player, Board board) {
        if (board == null)
            board = prepareBoard(DEFAULT_SIZE);
        createBoardState(player, board);
    }

    @Override
    public void start(Player player, int boardSize) {
        Board board = prepareBoard(boardSize);
        createBoardState(player, board);
    }

    @Override
    public boolean move(BoardState boardState, Position current, Position destination) {
        try {
            moveCommand.move(boardState, current, destination);
            startAI();
            return true;
        } catch (NoMoveException e) {
            logger.log(Logger.LogType.ERROR, e.getErrorMessage());
        }
        return false;
    }

    @Override
    public void updateBoardState(BoardState newBoardState, boolean isTreeFiller) {
        if (!isTreeFiller)
            sendBoardStateToController(newBoardState);
    }

    @Override
    public void endGame() {
        if (boardState.getBoard().getBlackLeft() == 0)
            controller.gameWon("white");
        else
            controller.gameWon("black");
    }


    //private class methods
    private Board prepareBoard(int boardSize) {
        if (boardSize % 2 == 1)
            throw new BoardSizeException("This size is not valid. It must be even.");

        Piece[][] pieces;
        int halfSize = boardSize / 2;
        final int EMPTY_ROW = 1; //halfSzie and this will create two empty rows
        pieces = new Piece[boardSize][boardSize];

        //fill board with unavailabe empty cells
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                pieces[i][j] = Piece.EMPTY_UNAVAILABLE;
            }
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i < (halfSize - EMPTY_ROW)) {
                    if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
                        pieces[i][j] = Piece.BLACK;
                } else if (i == halfSize - EMPTY_ROW || i == halfSize) {
                    if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
                        pieces[i][j] = Piece.EMPTY;
                } else if (i > halfSize) {
                    if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
                        pieces[i][j] = Piece.WHITE;
                }
            }
        }
        return new Board(pieces);
    }

    private void startAI(){
        TreeFiller treeFiller = new MinimaxTreeFiller(boardState, new MoveCommand(true), maxDepthForAI);
        treeFiller.fillTree();

        BoardState tree = algorithm.search(treeFiller.getCurrentNode(), true);
        Position[] positions = algorithm.getBestMove(boardState, tree);

        moveCommand.move(boardState, positions[0], positions[1]);
        // If the ai can do multiple turns then we have to start the ai again.
        if (!boardState.getPlayer().isMyTurn()) startAI();
    }

    private void createBoardState(Player player, Board board) {
        BoardState boardState = new BoardState(board, player);
        sendBoardStateToController(boardState);
    }

    private void sendBoardStateToController(BoardState boardState) {
        if (okayToShow) {
            this.boardState = boardState;
            controller.sendBoardStateToGui(boardState);
        }
    }


    // getters and setters
    public BoardState getBoardState() {
        return boardState;
    }

    public IMoveCommand getMoveCommand() {
        return moveCommand;
    }

    public void setMoveCommand(IMoveCommand moveCommand) {
        this.moveCommand = moveCommand;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public int getMaxDepthForAI() {
        return maxDepthForAI;
    }

    public void setMaxDepthForAI(int maxDepthForAI) {
        this.maxDepthForAI = maxDepthForAI;
    }
}
