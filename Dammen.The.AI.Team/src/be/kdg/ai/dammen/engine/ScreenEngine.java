package be.kdg.ai.dammen.engine;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.gui.Gui;
import be.kdg.ai.dammen.gui.GuiListener;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;
import be.kdg.ai.dammen.player.Player;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Sliman on 1-10-2015.
 */
public class ScreenEngine implements GuiListener {
    private Gui gui;
    private Board board;
    private GameEngine gameEngine;
    private boolean selected = false;
    private Piece currentSelectedPiece = null;
    private Piece selectedDestination = null;
    private Player playerBlack = null;
    private Player playerWhite = null;
    private ArrayList<Piece> forcedPieces = new ArrayList<Piece>();

    public void setGui(Gui gui){
        this.gui = gui;
    }
    public void setGameEngine(GameEngine gameEngine){
        this.gameEngine = gameEngine;
        playerBlack = new Player("Black-Player", TypePiece.Status.BLACK);
        playerWhite = new Player("White-Player", TypePiece.Status.WHITE);
    }

    protected void sendBoard(Board board){
        this.board = board;
        gui.showBoard(board);

    }
    @Override
    public void hover(int row, int column) {
        System.out.println("Row"+row +" Column"+column);
        Piece tijdelijk = board.getPieces()[row][column];
        System.out.println(tijdelijk.getStatus());
        System.out.println(tijdelijk.getRank());
    }

    @Override
    public void clicked(int row, int column) {
        System.out.println("Row"+row +" Column"+column);
        forceAttack();
        if (!selected)
        {
            System.out.println("selected");
            currentSelectedPiece = board.getPieces()[row][column];
            gui.changeColor(currentSelectedPiece, Color.RED);
            System.out.println("cur:"+currentSelectedPiece.getStatus());
            System.out.println("currank:"+currentSelectedPiece.getRank());
            selected = true;
        }else
        {
            System.out.println("destination");
            selectedDestination = board.getPieces()[row][column];
            gui.changeColor(currentSelectedPiece,Color.LIGHT_GRAY);
            System.out.println("dest:"+selectedDestination.getStatus());
            System.out.println("destrank:"+selectedDestination.getRank());
            if(currentSelectedPiece.getStatus() == TypePiece.Status.WHITE){
                System.out.println(forcedPieces.size());
                gameEngine.move(currentSelectedPiece, selectedDestination,forcedPieces,playerWhite);
            }
            if(currentSelectedPiece.getStatus() == TypePiece.Status.BLACK){
                System.out.println(forcedPieces.size());
                gameEngine.move(currentSelectedPiece, selectedDestination,forcedPieces,playerBlack);
            }
            selected = false;
        }
    }

    @Override
    public void newGame() {
        gameEngine.initializeGame();
    }

    public void forceAttack(){
        forcedPieces.clear();
        for (int i = 0; i<board.getSize(); i++){
            for (int j = 0; j<board.getSize();j++ ){
                if(j == 0){
                    if(board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE){
                        if(board.getPieces()[i-1][j+1].getStatus() == TypePiece.Status.BLACK){
                            if(board.getPieces()[i-2][j+2].getStatus() == TypePiece.Status.EMPTY){
                               // force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Wit moet aanvallen");
                            }
                        }
                    }else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK){
                        if(board.getPieces()[i+1][j+1].getStatus() == TypePiece.Status.WHITE){
                            if(board.getPieces()[i+2][j+2].getStatus() == TypePiece.Status.EMPTY){
                                // force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Zwart moet aanvallen");
                            }
                        }
                    }
                }else if(j == 9){
                    if(board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE){
                        if(board.getPieces()[i-1][j-1].getStatus() == TypePiece.Status.BLACK){
                            if(board.getPieces()[i-2][j-2].getStatus() == TypePiece.Status.EMPTY){
                                // force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Wit moet aanvallen");
                            }
                        }
                    }else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK){
                        if(board.getPieces()[i+1][j-1].getStatus() == TypePiece.Status.WHITE){
                            if(board.getPieces()[i+2][j-2].getStatus() == TypePiece.Status.EMPTY){
                                // force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Zwart moet aanvallen");
                            }
                        }
                    }
                }else {
                    if(board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE){
                        if(board.getPieces()[i-1][j+1].getStatus() == TypePiece.Status.BLACK){
                            if(board.getPieces()[i-2][j+2].getStatus() == TypePiece.Status.EMPTY){
                              //  force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Wit moet aanvallen");
                            }
                            // dan moet je aanvallen
                        }else if(board.getPieces()[i-1][j-1].getStatus() == TypePiece.Status.BLACK){
                            if(board.getPieces()[i-2][j-2].getStatus() == TypePiece.Status.EMPTY){
                              //  force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Wit moet aanvallen");
                            }
                            // dan moet je aanvallen
                        }

                    }else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK){
                        if(board.getPieces()[i+1][j+1].getStatus() == TypePiece.Status.WHITE){
                            if(board.getPieces()[i+2][j+2].getStatus() == TypePiece.Status.EMPTY){
                                //  force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Zwart moet aanvallen");
                            }
                            // dan moet je aanvallen
                        }else if(board.getPieces()[i+1][j-1].getStatus() == TypePiece.Status.WHITE  ){
                            if(board.getPieces()[i+2][j-2].getStatus() == TypePiece.Status.EMPTY){
                                //  force = true;
                                forcedPieces.add(board.getPieces()[i][j]);
                                System.out.println("Zwart moet aanvallen");
                            }
                            // dan moet je aanvallen
                        }

                    }

            }
        }
    }
        //return forcedPieces;
}
}
