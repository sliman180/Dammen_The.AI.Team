package be.kdg.ai.dammen.gui;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Ali Imran on 11/10/2015.
 */
public class ConsoleGui implements Gui {

    public void showBoard(Board board){


        System.out.println("?                                           ?");
        System.out.println("     A   B   C   D   E   F   G   H   I   J");
        for(int i = 0; i < board.getSize(); i++){
            switch (i)
            {
                case 0:System.out.print(" 01 ");
                    break;
                case 1:System.out.print(" 02 ");
                    break;
                case 2:System.out.print(" 03 ");
                    break;
                case 3:System.out.print(" 04 ");
                    break;
                case 4:System.out.print(" 05 ");
                    break;
                case 5:System.out.print(" 06 ");
                    break;
                case 6:System.out.print(" 07 ");
                    break;
                case 7:System.out.print(" 08 ");
                    break;
                case 8:System.out.print(" 09 ");
                    break;
                case 9:System.out.print(" 10 ");
            }
            for(int j =0; j< board.getSize();j++){
                if(board.getPieces()[i][j].getStatus() == TypePiece.Status.EMPTY){
                    System.out.printf("_~_|");
                }else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK){
                    System.out.printf("_X_|");

                }else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE){
                    System.out.printf("_O_|");

                }
            }

            System.out.println();
        }System.out.println("?                                           ?");
        System.out.println("black pieces left = "+board.getBlackLeft());
        System.out.println("white pieces left = "+board.getWhiteLeft());

    }

    @Override
    public void addListeners(GuiListener newListener) {

    }

    @Override
    public void notifyListeners() {

    }
}
