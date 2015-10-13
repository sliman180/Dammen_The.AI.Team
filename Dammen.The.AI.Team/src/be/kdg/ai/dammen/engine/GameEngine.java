package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.board.BoardFactory;
import be.kdg.ai.dammen.board.BoardListener;
import be.kdg.ai.dammen.gui.Gui;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class GameEngine implements BoardListener {
    private BoardFactory boardFactory;
    private Gui gui;
    private Board board;
    private static final int DIMENSION = 10;

    public void initializeGame(){
        boardFactory.createBoard(DIMENSION);
    }

    public void setBoardFactory(BoardFactory boardFactory){
        this.boardFactory = boardFactory;
    }

    public void setGui(Gui gui){
        this.gui = gui;
    }
    @Override
    public void isNewBoard(Board board) {
        this.board = board;
        refreshBoard();
    }

    private void refreshBoard(){
        gui.showBoard(board);
    }
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
        refreshBoard();
    }

    public void checkAttack(int getal1,int getal2,int getal3,int getal4, int speler){
        System.out.println(getal2 + " "+ getal1);
        System.out.println(getal4 + " "+ getal3);

        int a = getal4-getal2; //
        int b = getal3-getal1; //


        //  System.out.println("a="+a+" b="+b);

       /* if(speler ==1) {
            if (((getal4 - getal2) == a && (getal3 - getal1) == b)) {
                int coord1 = getal4 + 1; // getal4
                int coord2 = getal3 - 1; // getal3
                System.out.println(coord2 + " " + coord1);
                if (pieces[coord2 - 1][coord1].getStatus() == TypePiece.Status.BLACK) {
                    pieces[coord2 - 1][coord1] = new Board(TypePiece.Status.EMPTY);
                }
            }
        }
        */
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

    }
}
