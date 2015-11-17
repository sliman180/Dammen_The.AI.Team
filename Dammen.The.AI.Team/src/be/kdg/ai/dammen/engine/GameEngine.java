package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.board.BoardFactory;
import be.kdg.ai.dammen.board.BoardListener;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;
import be.kdg.ai.dammen.player.Player;

import java.util.ArrayList;

/**
 * Created by Sliman on 1-10-2015.
 */
public class GameEngine implements BoardListener {
    private BoardFactory boardFactory;
    private Board board;
    private ScreenEngine screenEngine;
    private final int POSITION_BEFORE = -1;
    private static final int DIMENSION = 10;
    private boolean attack = false;
    private boolean force = false;


    public void initializeGame(){
         boardFactory.createBoard(DIMENSION);

    }
    public void setScreenEngine(ScreenEngine screenEngine){
        this.screenEngine = screenEngine;
    }

    public void setBoardFactory(BoardFactory boardFactory){
        this.boardFactory = boardFactory;
    }


    @Override
    public void isNewBoard(Board board) {
        this.board = board;
        sendBoard();
    }


    private void sendBoard()
    {
        screenEngine.sendBoard(board);
    }

    //X is kolom, Y is rij
    public Piece getPiece(int row, int column){
        return board.getPieces()[row][column];
    }

    public void move(Piece currentPiece, Piece destinationPiece,ArrayList<Piece> forcedPieces, Player player){
        //forceAttack();
/**/
        //System.out.println(player.getName());
        if (currentPiece.getRank() == TypePiece.Rank.KING)
        {
            if(checkAmountBeforeSwapping(currentPiece,destinationPiece)){
                swap(currentPiece, destinationPiece);
                checkAttackForKing(currentPiece, destinationPiece);
            }

        } else if (currentPiece.getRank() == TypePiece.Rank.MAN)
        {
            if (player.getStatus() == TypePiece.Status.BLACK)
            {
                if (moveDown(currentPiece,destinationPiece) || moveUp(currentPiece,destinationPiece)) {
                    // swaps the currentpiece with the destinationPiece
                    if (forcedPieces.size() == 0){
                        swap(currentPiece, destinationPiece);
                        setRankToKing(destinationPiece);
                    }else {
                            if(forcedPieces.contains(currentPiece)){
                                swap(currentPiece, destinationPiece);
                                setRankToKing(destinationPiece);
                            }
                    }

                    // checks if the piece can be converted to king or not

                }
            }
            if (player.getStatus() == TypePiece.Status.WHITE)
            {
               // System.out.println("status");
                if (moveUp(currentPiece, destinationPiece) || moveDown(currentPiece,destinationPiece)) {
                    // swaps the currentpiece with the destinationPiece
                    if (forcedPieces.size() == 0){
                            swap(currentPiece, destinationPiece);
                            setRankToKing(destinationPiece);
                    }else {
                        if (forcedPieces.contains(currentPiece)){
                            swap(currentPiece, destinationPiece);
                            setRankToKing(destinationPiece);
                        }
                    }
                    // checks if the piece can be converted to king or not
                   // setRankToKing(destinationPiece);
                }
            }
        }

    }
    private boolean moveUp(Piece currentPiece, Piece destinationPiece)
    {
        if (destinationPiece.getColumn() == currentPiece.getColumn()+1 && destinationPiece.getRow() == currentPiece.getRow() -1)
        {
            return true;
        } else if (destinationPiece.getColumn() == currentPiece.getColumn()-1 && destinationPiece.getRow() == currentPiece.getRow() -1)
        {
            return true;
        }

        if (destinationPiece.getColumn() == currentPiece.getColumn()-2 && destinationPiece.getRow() == currentPiece.getRow() -2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()-1][currentPiece.getColumn()-1];
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY){
                checkAttack(currentPiece, destinationPiece);
                return attack;
            }
        }else if (destinationPiece.getColumn() == currentPiece.getColumn()+2 && destinationPiece.getRow() == currentPiece.getRow() -2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()-1][currentPiece.getColumn()+1];
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY){
                checkAttack(currentPiece, destinationPiece);
                return attack;
            }
        }
        return false;
    }
    private boolean moveDown(Piece currentPiece, Piece destinationPiece)
    {
        if (destinationPiece.getColumn() == currentPiece.getColumn()-1 && destinationPiece.getRow() == currentPiece.getRow() +1)
        {
            return true;
        } else if (destinationPiece.getColumn() == currentPiece.getColumn()+1 && destinationPiece.getRow() == currentPiece.getRow() +1)
        {
            return true;
        }

        if (destinationPiece.getColumn() == currentPiece.getColumn()-2 && destinationPiece.getRow() == currentPiece.getRow() +2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()+1][currentPiece.getColumn()-1];
            System.out.println(tempPiece.getStatus());
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY){
            checkAttack(currentPiece, destinationPiece);
            return attack;
            }
        }else if (destinationPiece.getColumn() == currentPiece.getColumn()+2 && destinationPiece.getRow() == currentPiece.getRow() +2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()+1][currentPiece.getColumn()+1];
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY) {
                checkAttack(currentPiece, destinationPiece);
                return attack;
            }
        }
        return false;
    }

    private void swap(Piece currentPiece, Piece destinationPiece)// kopieer destinationPiece piece naar een temp, destinationPiece vervangen door current, current vervangen door
    // temp, vervolgens de coordinaten terug veranderen van elke piece
    {
        if(currentPiece.getStatus()== TypePiece.Status.BLACK){
            if(destinationPiece.getStatus() == TypePiece.Status.EMPTY){
                currentPiece.setStatus(TypePiece.Status.EMPTY);
                destinationPiece.setStatus(TypePiece.Status.BLACK);
                System.out.println("Swapped");
                if(currentPiece.getRank() == TypePiece.Rank.KING){
                    currentPiece.setStatus(TypePiece.Status.EMPTY);
                    currentPiece.setRank(TypePiece.Rank.MAN);
                    destinationPiece.setStatus(TypePiece.Status.BLACK);
                    destinationPiece.setRank(TypePiece.Rank.KING);
                }
            }

        }else if(currentPiece.getStatus() == TypePiece.Status.WHITE){
            if(destinationPiece.getStatus() == TypePiece.Status.EMPTY){
                currentPiece.setStatus(TypePiece.Status.EMPTY);
                destinationPiece.setStatus(TypePiece.Status.WHITE);
                System.out.println("Swapped");

                if(currentPiece.getRank() == TypePiece.Rank.KING){
                    currentPiece.setStatus(TypePiece.Status.EMPTY);
                    currentPiece.setRank(TypePiece.Rank.MAN);
                    destinationPiece.setStatus(TypePiece.Status.WHITE);
                    destinationPiece.setRank(TypePiece.Rank.KING);
                }
            }
        }

        sendBoard();



    }

    public boolean checkAmountBeforeSwapping(Piece currentPiece, Piece destinationPiece){

        int verschilR = currentPiece.getRow() - destinationPiece.getRow();
        int verschilC = currentPiece.getColumn() - destinationPiece.getColumn();
        int count = 0;
                // 2 -2

        if(verschilR > 0 && verschilC < 0){
            for (int i = 1; i < verschilR+1;i++){
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <= 1){
                  //  System.out.println("11111 = "+board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].getRow());
                    if (destinationPiece.getColumn() == currentPiece.getColumn()+i && destinationPiece.getRow() == currentPiece.getRow()-i)
                    {
                        return true;
                    }

                }
            }
        }else if(verschilR > 0 && verschilC > 0){
            for (int i = 1; i < verschilR+1;i++){
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()-i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <= 1){

                    if (destinationPiece.getColumn() == currentPiece.getColumn()-i && destinationPiece.getRow() == currentPiece.getRow() -i)
                    {
                        return true;
                    }
                }
            }
        }else if(verschilR < 0 && verschilC > 0){
            int verschil = destinationPiece.getRow() - currentPiece.getRow();
            for (int i = 1; i < verschil+1;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <= 1){
                    if (destinationPiece.getColumn() == currentPiece.getColumn()-i && destinationPiece.getRow() == currentPiece.getRow() +i)
                    {
                        return true;
                    }
                }
            }
        }else if(verschilR < 0 && verschilC < 0){
            int verschil = destinationPiece.getRow() - currentPiece.getRow();
            for (int i = 1; i < verschil+1;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <=1){
                    if (destinationPiece.getColumn() == currentPiece.getColumn()+i && destinationPiece.getRow() == currentPiece.getRow() +i)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public void checkAttackForKing(Piece currentPiece, Piece destinationPiece){
        int verschilR = currentPiece.getRow() - destinationPiece.getRow();
        int verschilC = currentPiece.getColumn() - destinationPiece.getColumn();

        int count = 0;

        if(verschilR > 0 && verschilC > 0){
            for(int i = 0; i < verschilR;i++){
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()-i].getStatus() == TypePiece.Status.BLACK){
                    count++;
                }
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()-i].getStatus() == TypePiece.Status.WHITE){
                    count++;
                }
            }
            if(count == 1){
                for(int i = 0; i < verschilR;i++){
                    board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()-i].setStatus(TypePiece.Status.EMPTY);
                }
            }

        }
        if(verschilR > 0 && verschilC < 0){
            for(int i = 0; i < verschilR;i++){
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].getStatus() == TypePiece.Status.BLACK){
                    count++;
                }
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].getStatus() == TypePiece.Status.WHITE){
                    count++;
                }
            }

            if(count == 1){
                for(int i = 0; i < verschilR;i++){
                    board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].setStatus(TypePiece.Status.EMPTY);
                }
            }
        }
        if(verschilR < 0 && verschilC < 0){
            int verschil = destinationPiece.getRow() - currentPiece.getRow();
            for(int i = 0; i < verschil;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].getStatus() == TypePiece.Status.BLACK){
                    count++;
                }
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].getStatus() == TypePiece.Status.WHITE){
                    count++;
                }
            }

            if(count == 1){
                for(int i = 0; i < verschil;i++){
                    board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].setStatus(TypePiece.Status.EMPTY);
                }
            }
        }
        if(verschilR < 0 && verschilC > 0){
            int verschil = destinationPiece.getRow() - currentPiece.getRow();
            for(int i = 0; i < verschil;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].getStatus() == TypePiece.Status.BLACK){
                    count++;
                }
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].getStatus() == TypePiece.Status.WHITE){
                    count++;
                }
            }
            if(count == 1){
                for(int i = 0; i < verschil;i++){
                    board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].setStatus(TypePiece.Status.EMPTY);
                }
            }
        }
        sendBoard();

    }

    /**
     * checks if the currentPiece can attack, if it can attack the space between the destinationPiece and currentPiece will be empty
     */
    public void checkAttack(Piece currentPiece, Piece destinationPiece) {
        attack = false;
        int verschilRow = currentPiece.getRow() - destinationPiece.getRow();
        int verschilColumn = currentPiece.getColumn() - destinationPiece.getColumn();

        if (destinationPiece.getStatus() == TypePiece.Status.EMPTY) {
            if (verschilRow == 2 && verschilColumn == -2) {
                verschilRow = currentPiece.getRow() - 1;
                verschilColumn = currentPiece.getColumn() + 1;
                Piece tempPiece = board.getPieces()[verschilRow][verschilColumn];
                if(currentPiece.getStatus() == TypePiece.Status.WHITE){
                    if(tempPiece.getStatus() == TypePiece.Status.BLACK){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }else if(currentPiece.getStatus() == TypePiece.Status.BLACK){
                    if(tempPiece.getStatus() == TypePiece.Status.WHITE){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }

            } else if (verschilRow == -2 && verschilColumn == 2) {
                verschilRow = currentPiece.getRow() + 1;
                verschilColumn = currentPiece.getColumn() - 1;
                Piece tempPiece = board.getPieces()[verschilRow][verschilColumn];
                if(currentPiece.getStatus() == TypePiece.Status.WHITE){
                    if(tempPiece.getStatus() == TypePiece.Status.BLACK){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }else if(currentPiece.getStatus() == TypePiece.Status.BLACK) {
                    if (tempPiece.getStatus() == TypePiece.Status.WHITE) {
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }

            }else if (verschilRow == 2 && verschilColumn == 2) {
                verschilRow = currentPiece.getRow() - 1;
                verschilColumn = currentPiece.getColumn() - 1;
                Piece tempPiece = board.getPieces()[verschilRow][verschilColumn];
                if(currentPiece.getStatus() == TypePiece.Status.WHITE){
                    if(tempPiece.getStatus() == TypePiece.Status.BLACK){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }else if(currentPiece.getStatus() == TypePiece.Status.BLACK){
                    if(tempPiece.getStatus() == TypePiece.Status.WHITE){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }

            } else if (verschilRow == -2 && verschilColumn == -2) {
                verschilRow = currentPiece.getRow() + 1;
                verschilColumn = currentPiece.getColumn() + 1;
                Piece tempPiece = board.getPieces()[verschilRow][verschilColumn];
                if(currentPiece.getStatus() == TypePiece.Status.WHITE){
                    if(tempPiece.getStatus() == TypePiece.Status.BLACK){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }else if(currentPiece.getStatus() == TypePiece.Status.BLACK){
                    if(tempPiece.getStatus() == TypePiece.Status.WHITE){
                        tempPiece.setStatus(TypePiece.Status.EMPTY);
                        attack = true;
                        System.out.println("Attack");
                    }
                }

            }
         }
        sendBoard();

    }

    /**
     * checks if the destinated piece can be converted too a King piece
     */
    public void setRankToKing(Piece destinationPiece){
        Piece tempPiece = board.getPieces()[destinationPiece.getRow()][destinationPiece.getColumn()];
        if(tempPiece.getStatus() == TypePiece.Status.WHITE) {
            if (destinationPiece.getColumn() == 1 || destinationPiece.getColumn() == 3 || destinationPiece.getColumn() == 5 || destinationPiece.getColumn() == 7 || destinationPiece.getColumn() == 9) {
                if (destinationPiece.getRow() == 0) {
                    tempPiece.setRank(TypePiece.Rank.KING);
                }
            }
        }

        if(tempPiece.getStatus() == TypePiece.Status.BLACK) {
            if (destinationPiece.getColumn() == 0 || destinationPiece.getColumn() == 2 || destinationPiece.getColumn() == 4 || destinationPiece.getColumn() == 6 || destinationPiece.getColumn() == 8) {
                if (destinationPiece.getRow() == 9) {
                    tempPiece.setRank(TypePiece.Rank.KING);
                }
            }
        }

        // updates the board.
        sendBoard();
    }



}
