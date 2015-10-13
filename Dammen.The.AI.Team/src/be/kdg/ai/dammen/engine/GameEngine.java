package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class GameEngine implements Engine {
    private Board[][] board;

    public void setBoard(Board[][] board) {
        this.board = board;
    }



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

}
