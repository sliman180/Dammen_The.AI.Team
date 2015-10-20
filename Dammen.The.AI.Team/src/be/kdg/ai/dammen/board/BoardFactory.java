package be.kdg.ai.dammen.board;

import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sliman on 1-10-2015.
 */
public class BoardFactory {
    private Board board;
    private List<BoardListener> listeners;
    private Piece[][] pieces;

    public BoardFactory() {
        this.listeners = new ArrayList<BoardListener>();
    }

    public void addListeners(BoardListener newListener){
        listeners.add(newListener);
    }

    private void notifyListeners(){
        for (BoardListener listener: listeners){
            listener.isNewBoard(board);
        }
    }

    public void createBoard(int dimension) {
        pieces = new Piece[dimension][dimension];
        int blackLeft = 0;
        int whiteLeft = 0;
        
        for(int i = 0; i < pieces.length; i++){
            for(int j =0; j < pieces[i].length; j++){
                pieces[i][j] = new Piece(TypePiece.Status.EMPTY);
                setAttributesToPiece(i, j);
            }
        }

        for(int i = 0; i < pieces.length; i++){
            for(int j =0; j < pieces[i].length; j+=2){
                //pieces[i][j] = new Board(TypePiece.Status.BLACK);
                if(i < 4 ) {
                    if ((i % 2) == 0) {
                        //even
                        pieces[i][j + 1] = new Piece(TypePiece.Status.BLACK);
                        setAttributesToPiece(i, j);
                        blackLeft++;
                        //System.out.println("i:"+ i+ "---- j:"+j);
                    } else if ((i % 2) != 0) {
                        //odd
                        pieces[i][j] = new Piece(TypePiece.Status.BLACK);
                        setAttributesToPiece(i, j);
                        blackLeft++;
                    }
                }
           /*     if(i == 5){
                    pieces[i][j] = new Piece(TypePiece.Status.EMPTY);
                }else if(i == 4){
                    pieces[i][j+1] = new Piece(TypePiece.Status.EMPTY);
                }   */

                if(i > 5 && (i % 2) == 0){
                    pieces[i][j+1] = new Piece(TypePiece.Status.WHITE);
                    setAttributesToPiece(i, j);
                    whiteLeft++;
                }else if(i > 5 && (i % 2) != 0){
                    //odd
                    pieces[i][j] = new Piece(TypePiece.Status.WHITE);
                    setAttributesToPiece(i, j);
                    whiteLeft++;
                }
            }
        }
        board = new Board(pieces,dimension);
        board.setBlackLeft(blackLeft);
        board.setWhiteLeft(whiteLeft);
        notifyListeners();
    }
    private void setAttributesToPiece(int i, int j) // i = y; j = x
    {
        pieces[i][j].setColumn(j);
        pieces[i][j].setRow(i);
        pieces[i][j].setRank(TypePiece.Rank.MAN);
    }
}
