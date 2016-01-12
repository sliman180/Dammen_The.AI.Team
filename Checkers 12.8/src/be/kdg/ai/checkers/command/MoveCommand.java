package be.kdg.ai.checkers.command;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.board.Board;
import be.kdg.ai.checkers.domain.board.ForceAttack;
import be.kdg.ai.checkers.domain.board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * This class controls the movement of pieces
 */
public class MoveCommand implements IMoveCommand {
    private static final int MAX_MOVE_STEPS = 1;
    private static final int ATTACK_STEPS = 2;
    private static final int DIRECTIONS = 4;
    private int checkSteps;

    private final List<MoveListener> listeners;
    private BoardState boardState;
    private Position current, destination,temp;
    private Position forceDestinationPosition;
    private final boolean treeFiller;
    private Position tryEatPos;

    private boolean attackMode;
    private boolean searchingForcedAttacks;

    public MoveCommand(boolean treeFiller) {
        this.listeners = new ArrayList<>();
        attackMode = false;
        searchingForcedAttacks = false;
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
            System.arraycopy(boardState.getBoard().getPieces()[row], 0, pieces[row],
                    0, boardState.getBoard().getPieces()[row].length);
        }
        this.boardState = new BoardState(new Board(pieces), boardState.getPlayer());
        this.boardState.setForcedToAttackPieces(boardState.getForcedToAttackPieces());
        this.boardState.setOldforcedToAttackPieces(boardState.getOldforcedToAttackPieces());

        this.current = current;
        this.destination = destination;


        if (boardState.getForcedToAttackPieces().size() > 0 && !searchingForcedAttacks){
            if (checkCorrectForcedAttack(boardState, current, destination)) {
                attackMode = true;
                checkSteps = destination.getRow() - current.getRow();
                checkSteps = Math.abs(checkSteps);
                if (checkAttack() || checkAttackKing())
                    temp = new Position(destination.getRow(), destination.getColumn());
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
                    return checkMoveForKing(Piece.BLACK_KING);
                case WHITE_KING:
                    return checkMoveForKing(Piece.WHITE_KING);
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
            if((checkMoveDown() || checkMoveUp()) && checkAttackKing() && checkOverlapping()){
                swap();
                return true;
            }else{
                if((checkMoveDown() || checkMoveUp()) && checkOverlapping())
                    swap();
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
                searchForcedAttackOnePiece();
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

    private boolean checkOverlapping(){
        ArrayList<Piece> pieces = new ArrayList<>();

        tryEatPos = null;
        for(int i = 1; i < checkSteps;i++){
            findEatingPosition(i);
            if(tryEatPos!=null)
                if(boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE_KING){
                    pieces.add(Piece.WHITE_KING);
                }else if(boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK_KING){
                    pieces.add(Piece.BLACK_KING);
                }else if(boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE){
                    pieces.add(Piece.WHITE);
                }else if(boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK){
                    pieces.add(Piece.BLACK);
                }

        }

        if(boardState.getPlayer().isMyTurn()){
            if(pieces.contains(Piece.WHITE_KING)||pieces.contains(Piece.WHITE)){
                return false;
            }else if((pieces.contains(Piece.BLACK_KING)||pieces.contains(Piece.BLACK)) && pieces.size() > 1){
                return false;
            }
        }else if(!boardState.getPlayer().isMyTurn()){
            if(pieces.contains(Piece.BLACK_KING)||pieces.contains(Piece.BLACK)){
                return false;
            }else if((pieces.contains(Piece.WHITE_KING)||pieces.contains(Piece.WHITE)) && pieces.size() > 1){
                return false;
            }
        }

        return true;
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

    private boolean checkAttackKing(){
        if (!attackMode && !searchingForcedAttacks) return true;

        tryEatPos = null;
        boolean eatable = false;
        for(int i = 1; i < checkSteps;i++){
            findEatingPosition(i);
            if(checkOutOfBounds(tryEatPos)){
                tryEatPos = null;
            }
            if (tryEatPos != null) {
                if (boardState.getPlayer().isMyTurn()
                        && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK
                        || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK_KING)) {
                    eatable = true;
                } else if (!boardState.getPlayer().isMyTurn()
                        && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE
                        || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE_KING)) {
                    eatable = true;
                }else if(boardState.getPlayer().isMyTurn()
                        && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE
                        || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.WHITE_KING)){
                    eatable = false;
                }else if(!boardState.getPlayer().isMyTurn()
                        && (boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK
                        || boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] == Piece.BLACK_KING)){
                    eatable = false;
                }
                if (eatable && !searchingForcedAttacks) {
                    boardState.getBoard().getPieces()[tryEatPos.getRow()][tryEatPos.getColumn()] = Piece.EMPTY;
                }

            }

        }

        return eatable;
    }

    private void findEatingPosition(int i) {
        if (current.getRow() > destination.getRow() && current.getColumn() > destination.getColumn()) {
            tryEatPos = new Position(destination.getRow() + i, destination.getColumn() + i);
        } else if (current.getRow() > destination.getRow() && current.getColumn() < destination.getColumn()) {
            tryEatPos = new Position(destination.getRow() + i, destination.getColumn() - i);
        } else if (current.getRow() < destination.getRow() && current.getColumn() < destination.getColumn()) {
            tryEatPos = new Position(destination.getRow() - i, destination.getColumn() - i);
        } else if (current.getRow() < destination.getRow() && current.getColumn() > destination.getColumn()) {
            tryEatPos = new Position(destination.getRow() - i, destination.getColumn() + i);
        }
    }

    private void searchForcedAttackOnePiece(){
        if (temp != null && boardState != null && boardState.getBoard().getPieces() != null){
            ArrayList<ForceAttack> forcedAttackPieces = new ArrayList<>();
            searchingForcedAttacks = true;

            Piece piece = boardState.getBoard().getPieces()[temp.getRow()][temp.getColumn()];
            int steps = 2;
            if(!boardState.getPlayer().isMyTurn()){
                if (piece == Piece.BLACK) {
                    if (calculatePossibilityForceAttack(new Position(temp.getRow(), temp.getColumn()), steps)) {
                        forcedAttackPieces.add(new ForceAttack(new Position(temp.getRow(), temp.getColumn()), forceDestinationPosition));
                    }
                }else if(piece == Piece.BLACK_KING){
                    for(int i = steps; i < boardState.getBoard().getSize(); i++){
                        if (calculatePossibilityForceAttack(new Position(temp.getRow(), temp.getColumn()), i)) {
                            forcedAttackPieces.add(new ForceAttack(new Position(temp.getRow(), temp.getColumn()), forceDestinationPosition));
                        }
                    }
                }
            }else if (boardState.getPlayer().isMyTurn()) {
                if (piece == Piece.WHITE) {
                    if (calculatePossibilityForceAttack(new Position(temp.getRow(), temp.getColumn()), steps)) {
                        forcedAttackPieces.add(new ForceAttack(new Position(temp.getRow(), temp.getColumn()), forceDestinationPosition));
                    }
                }else if(piece == Piece.WHITE_KING){
                    for(int i = steps; i < boardState.getBoard().getSize(); i++){
                        if (calculatePossibilityForceAttack(new Position(temp.getRow(), temp.getColumn()), i)) {
                            forcedAttackPieces.add(new ForceAttack(new Position(temp.getRow(), temp.getColumn()), forceDestinationPosition));
                        }
                    }
                }

            }
            searchingForcedAttacks = false;
            boardState.setForcedToAttackPieces(forcedAttackPieces);
        }
    }

    private void searchForcedAttacks() {
        ArrayList<ForceAttack> forcedAttackPieces = new ArrayList<>();
        searchingForcedAttacks = true;

        for (int row = 0; row < boardState.getBoard().getSize(); row++) {
            for (int col = 0; col < boardState.getBoard().getSize(); col++) {
                Piece piece = boardState.getBoard().getPieces()[row][col];
                int steps = 2;

                if(!boardState.getPlayer().isMyTurn()){
                    if (piece == Piece.BLACK) {
                        if (calculatePossibilityForceAttack(new Position(row, col), steps)) {
                            forcedAttackPieces.add(new ForceAttack(new Position(row, col), forceDestinationPosition));
                        }
                    }else if(piece == Piece.BLACK_KING){
                        for(int i = steps; i < boardState.getBoard().getSize(); i++){
                            if (calculatePossibilityForceAttack(new Position(row, col), i)) {
                                forcedAttackPieces.add(new ForceAttack(new Position(row, col), forceDestinationPosition));
                            }
                        }
                    }
                }else if (boardState.getPlayer().isMyTurn()) {
                    if (piece == Piece.WHITE) {
                        if (calculatePossibilityForceAttack(new Position(row, col), steps)) {
                            forcedAttackPieces.add(new ForceAttack(new Position(row, col), forceDestinationPosition));
                        }
                    }else if(piece == Piece.WHITE_KING){
                        for(int i = steps; i < boardState.getBoard().getSize(); i++){
                            if (calculatePossibilityForceAttack(new Position(row, col), i)) {
                                forcedAttackPieces.add(new ForceAttack(new Position(row, col), forceDestinationPosition));
                            }
                        }
                    }

                }

            }
        }
        searchingForcedAttacks = false;
        // Remove the below comments to see every possible forced attack move
//        for (ForceAttack forceAttack : forcedAttackPieces) {
//            System.out.println("forced Attack Position    :   " + forceAttack.getCurrentPosition().getRow() + ", " + forceAttack.getCurrentPosition().getColumn() + " & Destination    :  "
//                    + forceAttack.getDestinationPosition().getRow() + ", " + forceAttack.getDestinationPosition().getColumn());
//        }
//        System.out.println("SIZE" + forcedAttackPieces.size());
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
        return position.getRow() >= boardState.getBoard().getSize() || position.getRow() < 0
                || position.getColumn() >= boardState.getBoard().getSize() || position.getColumn() < 0;
    }

}
