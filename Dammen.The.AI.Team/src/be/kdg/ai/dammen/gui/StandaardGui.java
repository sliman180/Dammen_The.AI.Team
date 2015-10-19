package be.kdg.ai.dammen.gui;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.piece.TypePiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sliman on 1-10-2015.
 */
public class StandaardGui extends JFrame implements Gui {
    private int width, height;
    private JPanel[][] cellArray;
    public  StandaardGui(){
        width = 500;
        height = 500;
    }

    @Override
    public void showBoard(Board board) {
        initializeJFrame();
        prepareBoard(board);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private void initializeJFrame(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Dammen");

        //Set the frame size, relative to the screen size.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x, y, width, height);
        setResizable(false);
    }

    private void prepareBoard(Board board){
        cellArray = new JPanel[board.getSize()][board.getSize()];
        JPanel boardBackground = new JPanel();
        boardBackground.setLayout(new GridLayout(board.getSize(), board.getSize()));
        boardBackground.setBackground(new Color(245, 222, 179)); //wheat color

        // Load images of pieces
        BufferedImage whitePiece = null;
        BufferedImage blackPiece = null;

        JLabel whitePieceLabel;
        JLabel blackPieceLabel;
        try {
            whitePiece = ImageIO.read(new File("images\\krem_.png"));
            blackPiece = ImageIO.read(new File("images\\sedefli_mavi.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < board.getSize(); i++){
            for(int j =0; j< board.getSize();j++){
                JPanel cell = new JPanel();
                cellArray[i][j] = cell;

                if(i % 2 == 0){
                    if(j % 2 == 0){
                        cell.setBackground(Color.WHITE);
                        boardBackground.add(cell);
                    }
                    else {
                        cell.setBackground(Color.LIGHT_GRAY);
                        boardBackground.add(cell);
                    }
                }
                else {
                    if(j % 2 == 0){
                        cell.setBackground(Color.LIGHT_GRAY);
                        boardBackground.add(cell);
                    }
                    else {
                        cell.setBackground(Color.WHITE);
                        boardBackground.add(cell);
                    }
                }
                if(board.getPieces()[i][j].getStatus() == TypePiece.Status.EMPTY){

                }
                else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK){
                    // the width is divided by 13 to make the piece fit in the cell
                    ImageIcon blackPieceImageIcon = new ImageIcon(blackPiece.getScaledInstance(width/13,width/13,width/13));
                    blackPieceLabel = new JLabel(blackPieceImageIcon);
                    cell.add(blackPieceLabel);
                }
                else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE){
                    // the width is divided by 13 to make the piece fit in the cell
                    ImageIcon whitePieceImageIcon = new ImageIcon(whitePiece.getScaledInstance(width/13,width/13,width/13));
                    whitePieceLabel = new JLabel(whitePieceImageIcon);
                    cell.add(whitePieceLabel);
                }
            }

            System.out.println();
        }

        add(boardBackground);
    }
}
