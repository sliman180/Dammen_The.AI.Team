package be.kdg.ai.checkers.command;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.board.Board;
import be.kdg.ai.checkers.domain.board.ForceAttack;
import be.kdg.ai.checkers.domain.board.Position;

import java.util.*;

/**
 * Created by Sayu on 24/11/2015.
 */
public class MoveCommand implements IMoveCommand {
    private final int MAX_MOVE_STEPS = 1;
    private final int ATTACK_STEPS = 2;
    private final int DIRECTIONS = 4;
    private int checkSteps;

    private List<MoveListener> listeners;
    private BoardState boardState;
    private Position current, destination;
    private Position forceDestinationPosition;
    private boolean treeFiller;

    private boolean attackMode, searchingForcedAttacks, swappable;

    public MoveCommand(boolean treeFiller) {
        this.listeners = new ArrayList<>();
        attackMode = false;
        searchingForcedAttacks = false;
        swappable = false;
        forceDestinationPosition = null;
        this.treeFiller = treeFiller;
    }

    @Override
    public void addListener(MoveListener listenerToAdd) {
        listeners.add(listenerToAdd);
    }

    private void notifyListeners() {
        for (MoveListener listener : listeners) {
            listener.updateBoardState(boardState, treeFiller);
        }
    }
    private void notifyEndGame(){
        for (MoveListener listener : listeners) {
            listener.endGame();
        }
    }

    @Override
    public boolean move(BoardState boardState, Position current, Position destination) throws NoMoveException{
        Piece[][] pieces = new Piece[boardState.getBoard().getSize()][boardState.getBoard().getSize()];
        for (int row = 0; row < boardState.getBoard().getPieces().length; row++) {
            for (int col = 0; col < boardState.getBoard().getPieces()[row].length; col++) {
                pieces[row][col] = boardState.getBoard().getPieces()[row][col];
            }
        }
        this.boardState = new BoardState(new Board(pieces), boardState.getPlayer());
        this.boardState.setForcedToAttackPieces(boardState.getForcedToAttackPieces());
        this.boardState.setOldforcedToAttackPieces(boardState.getOldforcedToAttackPieces());

        this.current = current;
        this.destination = destination;
        swappable = false;


        if (boardState.getForcedToAttackPieces().size() > 0 && !searchingForcedAttacks){
            if (checkCorrectForcedAttack(boardState, current, destination)) {
                attackMode = true;
                if (checkAttack() || checkAttackKing())
                    swap();
            }
        } else {
            if (boardState.getBoard().getPieces()[destination.getRow()][destination.getColumn()] != Piece.EMPTY)
                throw new NoMoveException("Your destination is not empty.");
            if (current.getRow() == destination.getRow() || current.getColumn() == destination.getColumn())
                throw new NoMoveException("Horizontal move or vertical moves cannot be done.");


            //an ordinary move should always be 1 or -1
            if ((current.getRow() - destination.getRow() == MAX_MOVE_STEPS)
                    || (current.getRow() - destination.getRow() == -MAX_MOVE_STEPS)) {
                attackMode = false;
                checkSteps = MAX_MOVE_STEPS;
            } else {
                attackMode = true;
                checkSteps = ATTACK_STEPS;
            }

            switch (boardState.getBoard().getPieces()[current.getRow()][current.getColumn()]) {
                case BLACK_KING:
                    checkMoveForKing(Piece.BLACK_KING);
                    break;
                case WHITE_KING:
                    checkMoveForKing(Piece.WHITE_KING);
                    break;
                case WHITE:
                    return checkMove(Piece.WHITE);
                case BLACK:
                    return checkMove(Piece.BLACK);
                default:
                    break;
            }
        }
        return false;
    }

    private boolean checkCorrectForcedAttack(BoardState boardState, Position current, Position destination) {
        return boardState.getForcedToAttackPieces().contains(new ForceAttack(current, destination));
    }

    private boolean checkMoveForKing(Piece color) {
        checkSteps = current.getRow() - destination.getRow();
        checkSteps = Math.abs(checkSteps);

        if(color == Piece.WHITE_KING || color == Piece.BLACK_KING){
            if((checkMoveDown() || checkMoveUp()) && checkAttackKing()){
                swap();
                return true;
            }
        }
        return false;
    }

    private boolean checkMove(Piece color) {
        if (color == Piece.WHITE) {
            if (checkMoveUp() && checkAttack()) {
                swap();
                return true;
            }
        } else if(color == Piece.BLACK) {
            if (checkMoveDown() && checkAttack()) {
                swap();
                return true;
            }
        }
        checkForUpgrade();
        return false;
    }


    private boolean checkMoveUp() {
        if (destination.getColumn() == current.getColumn() + checkSteps && destination.getRow() == current.getRow() - checkSteps) {
            return true;
        } else if (destination.getColumn() == current.getColumn() - checkSteps && destination.getRow() == current.getRow() - checkSteps) {
            return true;
        }
        return false;
    }

    private boolean checkMoveDown() {
        if (destination.getColumn() == current.getColumn() - checkSteps && destination.getRow() == current.getRow() + checkSteps) {
            return true;
        } else if (destination.getColumn() == current.getColumn() + checkSteps && destination.getRow() == current.getRow() + checkSteps) {
            return true;
        }
        return false;
    }

    private void swap() {
        if (!searchingForcedAttacks) {
            Piece tempPiece = boardState.getBoard().getPieces()[current.getRow()][current.getColumn()];
            boardState.getBoard().getPieces()[destination.getRow()][destination.getColumn()] = tempPiece;
            boardState.getBoard().getPieces()[current.getRow()][current.getColumn()] = Piece.EMPTY;

            if(boardState.getBoard().getBlackLeft() == 0 || boardState.getBoard().getWhiteLeft() == 0){
                notifyEndGame();
            }

            if (attackMode)
            {
                searchForcedAttacks();
                if(boardState.getForcedToAttackPieces().size() == 0){
                    boardState.getPlayer().toggleTurn();
                }
            }else{
                boardState.getPlayer().toggleTurn();
                //searchForcedAttacks();
            }
           // boardState.getPlayer().toggleTurn();
            // set the steps to ordinary step = 1
            checkSteps = MAX_MOVE_STEPS;
            searchForcedAttacks();
            notifyListeners();
        }
    }

    private void checkForUpgrade() {
        int rowWhite = boardState.getBoard().getSize() - 1;
        int rowBlack = 0;
        for(int i = 1; i < boardState.getBoard().getSize(); i+=2){
            if(boardState.getBoard().getPieces()[rowBlack][i] == Piece.WHITE){
                boardState.getBoard().getPieces()[rowBlack][i] = Piece.WHITE_KING;
                notifyListeners();
            }
        }
        for(int i = 0; i < boardState.getBoard().getSize(); i+=2){
            if(boardState.getBoard().getPieces()[rowWhite][i] == Piece.BLACK){
                boardState.getBoard().getPieces()[rowWhite][i] = Piece.BLACK_KING;
                notifyListeners();
            }
        }
    }

    private boolean checkAttack() {
        if (!attackMode && !searchingForcedAttacks) return true;
      //  System.out.println(boardState.getPlayer().isMyTurn());
        Position tryEatPos = null;

            if (current.getRow() > destination.getRow() && current.getColumn() > destination.getColumn()) {
                tryEatPos = new Position(destination.getRow() + 1, destination.getColumn() + 1);
            } else if (current.getRow() > destination.getRow() && current.getColumn() < destination.getColumn()) {
                tryEatPos = new Position(destination.getRow() + 1, destination.getColumn() - 1);
            } else if (current.getRow() < destination.getRow() && current.getColumn() < destination.getColumn()) {
                tryEatPos = new Position(destination.getRow() - 1, destination.getColumn() - 1);
            } else if (current.getRow() < destination.getRow() && current.getColumn() > destination.getColumn()) {
                tryEatPos = new Position(destination.getRow() - 1, destination.getColumn() + 1);
            }



        boolean eatable = false;

        if (tryEatPos != null) {
            if (boardState.getPlayer().isMyTurn()
                    && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK
                    || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK_KING)) {
                eatable = true;
//                System.out.println("eatable for white:" + tryEatPos.getRow() + " : " + tryEatPos.getColumn());
            } else if (!boardState.getPlayer().isMyTurn()
                    && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE
                    || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE_KING)) {
                eatable = true;
//                System.out.println("eatable for black:" + tryEatPos.getRow() + " : " + tryEatPos.getColumn());
            }

            if (eatable && !searchingForcedAttacks) {
                boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] = Piece.EMPTY;
            }
        }
        return eatable;
    }

    public boolean checkAttackKing(){
        if (!attackMode && !searchingForcedAttacks) return true;

        Position tryEatPos = null;
        boolean eatable = false;

            for(int i = 1; i < checkSteps;i++){
                if (current.getRow() > destination.getRow() && current.getColumn() > destination.getColumn()) {
                    tryEatPos = new Position(destination.getRow() + i, destination.getColumn() + i);
                } else if (current.getRow() > destination.getRow() && current.getColumn() < destination.getColumn()) {
                    tryEatPos = new Position(destination.getRow() + i, destination.getColumn() - i);
                } else if (current.getRow() < destination.getRow() && current.getColumn() < destination.getColumn()) {
                    tryEatPos = new Position(destination.getRow() - i, destination.getColumn() - i);
                } else if (current.getRow() < destination.getRow() && current.getColumn() > destination.getColumn()) {
                    tryEatPos = new Position(destination.getRow() - i, destination.getColumn() + i);
                }

                if (tryEatPos != null) {
                    if (boardState.getPlayer().isMyTurn()
                            && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK
                            || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK_KING)) {
                        eatable = true;
//                        System.out.println("eatable for white:" + tryEatPos.getRow() + " : " + tryEatPos.getColumn());
                    } else if (!boardState.getPlayer().isMyTurn()
                            && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE
                            || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE_KING)) {
                        eatable = true;
//                        System.out.println("eatable for blacks:" + tryEatPos.getRow() + " : " + tryEatPos.getColumn());
                    }else if(boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.EMPTY){
                        eatable = true;
                    }
                    if (eatable && !searchingForcedAttacks) {
                        boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] = Piece.EMPTY;
                    }
                }
            }

        return eatable;
    }

    private void searchForcedAttacks() {
        ArrayList<ForceAttack> forcedAttackPieces = new ArrayList<>();
        searchingForcedAttacks = true;
        int steps = 2;

        for (int row = 0; row < boardState.getBoard().getSize(); row++) {
            for (int col = 0; col < boardState.getBoard().getSize(); col++) {
                Piece piece = boardState.getBoard().getPieces()[row][col];

                if (boardState.getPlayer().isMyTurn()) {
                    if (piece == Piece.WHITE || piece == Piece.WHITE_KING) {
                        if (calculatePossibilityForceAttack(new Position(row, col), steps)) {
                            forcedAttackPieces.add(new ForceAttack(new Position(row, col), forceDestinationPosition));
                        }
                    }

                } else {
                    if (piece == Piece.BLACK || piece == Piece.BLACK_KING) {
                        if (calculatePossibilityForceAttack(new Position(row, col), steps)) {
                            forcedAttackPieces.add(new ForceAttack(new Position(row, col), forceDestinationPosition));
                        }
                    }
                }

            }
        }
        searchingForcedAttacks = false;
        boardState.setForcedToAttackPieces(forcedAttackPieces);
    }

    private boolean calculatePossibilityForceAttack(Position startingPosition, int steps) {
        for (int i = 0; i < DIRECTIONS; i++) {
            switch (i) {
                case 0:
                    forceDestinationPosition = new Position(startingPosition.getRow() + steps, startingPosition.getColumn() + steps);
                    break;
                case 1:
                    forceDestinationPosition = new Position(startingPosition.getRow() + steps, startingPosition.getColumn() - steps);
                    break;
                case 2:
                    forceDestinationPosition = new Position(startingPosition.getRow() - steps, startingPosition.getColumn() - steps);
                    break;
                case 3:
                    forceDestinationPosition = new Position(startingPosition.getRow() - steps, startingPosition.getColumn() + steps);
                    break;
                default:
                    break;
            }
            if (checkForceAttackPossibility(startingPosition))
                return true;
        }
        return false;
    }

    private boolean checkForceAttackPossibility(Position startingPosition) {
        if (!checkOutOfBounds(forceDestinationPosition)) {
            try {
                return move(boardState, startingPosition, forceDestinationPosition);
            }catch(NoMoveException e){
                //no need to log this, it'll trigger a lot
            }
        }
        return false;
    }

    private boolean checkOutOfBounds(Position position) {
        if (position.getRow() >= boardState.getBoard().getSize() || position.getRow() < 0
                || position.getColumn() >= boardState.getBoard().getSize() || position.getColumn() < 0) {
            return true;
        }
        return false;
    }

}
