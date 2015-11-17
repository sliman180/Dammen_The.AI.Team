package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.board.BoardFactory;
import be.kdg.ai.dammen.board.BoardListener;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;
import be.kdg.ai.dammen.player.Player;

/**
 * Created by Sliman on 1-10-2015.
 */
public class GameEngine implements BoardListener {
    private BoardFactory boardFactory;
    private Board board;
    private ScreenEngine screenEngine;

    private static final int DIMENSION = 10;
    private boolean attack = false;


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

    public void move(Piece currentPiece, Piece destination, Player player){

/**/
        //System.out.println(player.getName());
        if (currentPiece.getRank() == TypePiece.Rank.KING)
        {
            if(checkAmountBeforeSwapping(currentPiece,destination)){
                swap(currentPiece, destination);
                checkAttackForKing(currentPiece,destination);
            }

        } else if (currentPiece.getRank() == TypePiece.Rank.MAN)
        {
            if (player.getStatus() == TypePiece.Status.BLACK)
            {
                if (moveDown(currentPiece,destination) || moveUp(currentPiece,destination)) {
                    swap(currentPiece, destination);
                    checkRank(destination);
                }
            }
            if (player.getStatus() == TypePiece.Status.WHITE)
            {
                System.out.println("status");
                if (moveUp(currentPiece,destination) || moveDown(currentPiece,destination)) {
                    swap(currentPiece, destination);
                    checkRank(destination);
                }
            }
        }

    }
    private boolean moveUp(Piece currentPiece, Piece destination)
    {
        if (destination.getColumn() == currentPiece.getColumn()+1 && destination.getRow() == currentPiece.getRow() -1)
        {
            return true;
        } else if (destination.getColumn() == currentPiece.getColumn()-1 && destination.getRow() == currentPiece.getRow() -1)
        {
            return true;
        }

        if (destination.getColumn() == currentPiece.getColumn()-2 && destination.getRow() == currentPiece.getRow() -2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()-1][currentPiece.getColumn()-1];
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY){
                checkAttack(currentPiece, destination);
                return attack;
            }
        }else if (destination.getColumn() == currentPiece.getColumn()+2 && destination.getRow() == currentPiece.getRow() -2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()-1][currentPiece.getColumn()+1];
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY){
                checkAttack(currentPiece, destination);
                return attack;
            }
        }
        return false;
    }
    private boolean moveDown(Piece currentPiece, Piece destination)
    {
        if (destination.getColumn() == currentPiece.getColumn()-1 && destination.getRow() == currentPiece.getRow() +1)
        {
            return true;
        } else if (destination.getColumn() == currentPiece.getColumn()+1 && destination.getRow() == currentPiece.getRow() +1)
        {
            return true;
        }

        if (destination.getColumn() == currentPiece.getColumn()-2 && destination.getRow() == currentPiece.getRow() +2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()+1][currentPiece.getColumn()-1];
            System.out.println(tempPiece.getStatus());
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY){
            checkAttack(currentPiece, destination);
            return attack;
            }
        }else if (destination.getColumn() == currentPiece.getColumn()+2 && destination.getRow() == currentPiece.getRow() +2)
        {
            Piece tempPiece = board.getPieces()[currentPiece.getRow()+1][currentPiece.getColumn()+1];
            if(tempPiece.getStatus() != TypePiece.Status.EMPTY) {
                checkAttack(currentPiece, destination);
                return attack;
            }
        }
        return false;
    }

    private void swap(Piece currentPiece, Piece destination)// kopieer destination piece naar een temp, destination vervangen door current, current vervangen door
    // temp, vervolgens de coordinaten terug veranderen van elke piece
    {   /*
        Piece emptyTempPiece = board.getPieces()[destination.getRow()][destination.getColumn()];
        // destination
        board.getPieces()[destination.getRow()][destination.getColumn()] = currentPiece;
        emptyTempPiece.setColumn(currentPiece.getColumn());
        emptyTempPiece.setRow(currentPiece.getRow());
        currentPiece.setColumn(destination.getColumn());
        currentPiece.setRow(destination.getRow());
        board.getPieces()[emptyTempPiece.getRow()][emptyTempPiece.getColumn()] = emptyTempPiece;
        */
        if(currentPiece.getStatus()== TypePiece.Status.BLACK){
            if(destination.getStatus() == TypePiece.Status.EMPTY){
                currentPiece.setStatus(TypePiece.Status.EMPTY);
                destination.setStatus(TypePiece.Status.BLACK);

                System.out.println("Swapped");
                if(currentPiece.getRank() == TypePiece.Rank.KING){
                    currentPiece.setStatus(TypePiece.Status.EMPTY);
                    currentPiece.setRank(TypePiece.Rank.MAN);
                    destination.setStatus(TypePiece.Status.BLACK);
                    destination.setRank(TypePiece.Rank.KING);
                }
            }

        }else if(currentPiece.getStatus() == TypePiece.Status.WHITE){
            if(destination.getStatus() == TypePiece.Status.EMPTY){
                currentPiece.setStatus(TypePiece.Status.EMPTY);
                destination.setStatus(TypePiece.Status.WHITE);
                System.out.println("Swapped");

                if(currentPiece.getRank() == TypePiece.Rank.KING){
                    currentPiece.setStatus(TypePiece.Status.EMPTY);
                    currentPiece.setRank(TypePiece.Rank.MAN);
                    destination.setStatus(TypePiece.Status.WHITE);
                    destination.setRank(TypePiece.Rank.KING);
                }
            }
        }

        sendBoard();



    }

    public boolean checkAmountBeforeSwapping(Piece currentPiece, Piece destination){

        int verschilR = currentPiece.getRow() - destination.getRow();
        int verschilC = currentPiece.getColumn() - destination.getColumn();
        int count = 0;
                // 2 -2

        if(verschilR > 0 && verschilC < 0){
            for (int i = 1; i < verschilR+1;i++){
                if(board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <= 1){
                  //  System.out.println("11111 = "+board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].getRow());
                    if (destination.getColumn() == currentPiece.getColumn()+i && destination.getRow() == currentPiece.getRow()-i)
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

                    if (destination.getColumn() == currentPiece.getColumn()-i && destination.getRow() == currentPiece.getRow() -i)
                    {
                        return true;
                    }
                }
            }
        }else if(verschilR < 0 && verschilC > 0){
            int verschil = destination.getRow() - currentPiece.getRow();
            for (int i = 1; i < verschil+1;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <= 1){
                    if (destination.getColumn() == currentPiece.getColumn()-i && destination.getRow() == currentPiece.getRow() +i)
                    {
                        return true;
                    }
                }
            }
        }else if(verschilR < 0 && verschilC < 0){
            int verschil = destination.getRow() - currentPiece.getRow();
            for (int i = 1; i < verschil+1;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].getStatus() != TypePiece.Status.EMPTY){
                    count++;
                }
                if(count <=1){
                    if (destination.getColumn() == currentPiece.getColumn()+i && destination.getRow() == currentPiece.getRow() +i)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public void checkAttackForKing(Piece currentPiece, Piece destination){
        int verschilR = currentPiece.getRow() - destination.getRow();
        int verschilC = currentPiece.getColumn() - destination.getColumn();

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
               // swap(currentPiece, destination);
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
               //swap(currentPiece, destination);
                for(int i = 0; i < verschilR;i++){
                    board.getPieces()[currentPiece.getRow()-i][currentPiece.getColumn()+i].setStatus(TypePiece.Status.EMPTY);
                }
            }
        }
        if(verschilR < 0 && verschilC < 0){
            int verschil = destination.getRow() - currentPiece.getRow();
            for(int i = 0; i < verschil;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].getStatus() == TypePiece.Status.BLACK){
                    count++;
                }
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].getStatus() == TypePiece.Status.WHITE){
                    count++;
                }
            }

            if(count == 1){
                //swap(currentPiece, destination);
                for(int i = 0; i < verschil;i++){
                    board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()+i].setStatus(TypePiece.Status.EMPTY);
                }
            }
        }
        if(verschilR < 0 && verschilC > 0){
            int verschil = destination.getRow() - currentPiece.getRow();
            for(int i = 0; i < verschil;i++){
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].getStatus() == TypePiece.Status.BLACK){
                    count++;
                }
                if(board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].getStatus() == TypePiece.Status.WHITE){
                    count++;
                }
            }
            if(count == 1){
                //swap(currentPiece, destination);
                for(int i = 0; i < verschil;i++){
                    board.getPieces()[currentPiece.getRow()+i][currentPiece.getColumn()-i].setStatus(TypePiece.Status.EMPTY);
                }
            }
        }
        sendBoard();

    }
    public void checkAttack(Piece currentPiece, Piece destination) {

        int verschilRow = currentPiece.getRow() - destination.getRow();
        int verschilColumn = currentPiece.getColumn() - destination.getColumn();

        if (destination.getStatus() == TypePiece.Status.EMPTY) {
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

    public void checkRank(Piece destination){
        Piece tempPiece = board.getPieces()[destination.getRow()][destination.getColumn()];
        if(tempPiece.getStatus() == TypePiece.Status.WHITE) {
            if (destination.getColumn() == 1 || destination.getColumn() == 3 || destination.getColumn() == 5 || destination.getColumn() == 7 || destination.getColumn() == 9) {
                if (destination.getRow() == 0) {
                    tempPiece.setRank(TypePiece.Rank.KING);
                }
            }
        }

        if(tempPiece.getStatus() == TypePiece.Status.BLACK) {
            if (destination.getColumn() == 0 || destination.getColumn() == 2 || destination.getColumn() == 4 || destination.getColumn() == 6 || destination.getColumn() == 8) {
                if (destination.getRow() == 9) {
                    tempPiece.setRank(TypePiece.Rank.KING);
                }
            }
        }

        sendBoard();

    }


/*
    public void checkAttack(int getal1,int getal2,int getal3,int getal4, int speler){
        System.out.println(getal2 + " "+ getal1);
        System.out.println(getal4 + " "+ getal3);

        int a = getal4-getal2; //
        int b = getal3-getal1; //


        //  System.out.println("a="+a+" b="+b);

       if(speler ==1) {
            if (((getal4 - getal2) == a && (getal3 - getal1) == b)) {
                int coord1 = getal4 + 1; // getal4
                int coord2 = getal3 - 1; // getal3
                System.out.println(coord2 + " " + coord1);
                if (pieces[coord2 - 1][coord1].getStatus() == TypePiece.Status.BLACK) {
                    pieces[coord2 - 1][coord1] = new Board(TypePiece.Status.EMPTY);
                }
            }
        }

        if(a >= 2 && b >=2){
            if(((getal4-getal2) == a && (getal3-getal1) == b)){
                int coord1 = getal4 - (a-1);
                int coord2 = getal3 - (b-1);
                System.out.println(coord1 +" " + coord2);
                if(board.getPieces()[coord2-1][coord1].getStatus() ==TypePiece.Status.BLACK){
                    board.getPieces()[coord2-1][coord1] = new Piece(TypePiece.Status.EMPTY);}
                if(board.getPieces()[coord2-1][coord1].getStatus() ==TypePiece.Status.WHITE){
                    board.getPieces()[coord2-1][coord1] = new Piece(TypePiece.Status.EMPTY);}
            }
        }

        // attack backwards
        if(a <= -2 && b <= -2){
            if(((getal4-getal2)== a && (getal3-getal1) == b)){
                int coord1 = getal4 + (a+1);
                int coord2 = getal3 + (b+1);
                System.out.println(coord1 +" " + coord2);
                if(board.getPieces()[coord2-1][coord1].getStatus() ==TypePiece.Status.BLACK){
                    board.getPieces()[coord2-1][coord1] = new Piece(TypePiece.Status.EMPTY);}
                if(board.getPieces()[coord2-1][coord1].getStatus() ==TypePiece.Status.WHITE){
                    board.getPieces()[coord2-1][coord1] = new Piece(TypePiece.Status.EMPTY);}
            }
        }

    }*/

    /*
    public void doeZet(String coordinaatOudePion, String coordinaatNieuwePion, int speler) {
        // boolean bezet = false;
        String letter = coordinaatNieuwePion.substring(0,1);
        String letter2 = coordinaatOudePion.substring(0,1);
        char temp[] = letter.toLowerCase().toCharArray();
        char temp2[] = letter2.toLowerCase().toCharArray();
        char cletter = temp[0];
        char cletter2 = temp2[0];
        int getal1, getal2, getal4, getal3;
        getal2 = cletter - 'a';
        getal1 = Integer.parseInt(coordinaatNieuwePion.substring(1,3));

        if(speler == 1){
            board.getPieces()[getal1-1][getal2] = new Piece(TypePiece.Status.WHITE);
        }else if(speler == 2){
            board.getPieces()[getal1-1][getal2] = new Piece(TypePiece.Status.BLACK);
        }

        getal4 = cletter2 - 'a';
        getal3 = Integer.parseInt(coordinaatOudePion.substring(1, 3));

        board.getPieces()[getal3-1][getal4] = new Piece(TypePiece.Status.EMPTY);

        checkAttack(getal1,getal2,getal3,getal4,speler);
        sendBoard();
    }
*/
}
