package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class GameEngine implements Engine {
    public static final int X = 10;
    public static final int Y = 10;
    private Board[][] board = new Board[X][Y];

    @Override
    public void createBoard() {
        for(int i = 0; i < board.length; i++){
            for(int j =0; j < board[i].length; j++){
                board[i][j] = new Board(TypePiece.Status.EMPTY);

            }
        }

        for(int i = 0; i < board.length; i++){
            for(int j =0; j < board[i].length; j+=2){
                //board[i][j] = new Board(TypePiece.Status.BLACK);
                if((i % 2) == 0){
                    //even
                    board[i][j+1] = new Board(TypePiece.Status.BLACK);
                }else if((i % 2) != 0){
                    //odd
                    board[i][j] = new Board(TypePiece.Status.BLACK);
                }

                if(i == 5){
                    board[i][j] = new Board(TypePiece.Status.EMPTY);
                }else if(i == 4){
                    board[i][j+1] = new Board(TypePiece.Status.EMPTY);
                }

                if(i > 5 && (i % 2) == 0){
                    board[i][j+1] = new Board(TypePiece.Status.WHITE);
                }else if(i > 5 && (i % 2) != 0){
                    //odd
                    board[i][j] = new Board(TypePiece.Status.WHITE);
                }
            }
        }
    }

    public void showBoard(){
        System.out.println("▛                                           ▜");
        System.out.println("     A   B   C   D   E   F   G   H   I   J");
        for(int i = 0; i < X; i++){
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
            for(int j =0; j< Y;j++){
                if(board[i][j].getStatus() == TypePiece.Status.EMPTY){
                    System.out.printf("_~_|");
                }else if(board[i][j].getStatus() == TypePiece.Status.BLACK){
                    System.out.printf("_X_|");
                }else if(board[i][j].getStatus() == TypePiece.Status.WHITE){
                    System.out.printf("_O_|");
                }

            }
            System.out.println();
        }System.out.println("▙                                           ▟");
    }

}
