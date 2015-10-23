package be.kdg.ai.dammen.gui;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.piece.Piece;
import be.kdg.ai.dammen.piece.TypePiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Sliman on 1-10-2015.
 */
public class StandaardGui extends JFrame implements Gui {
    protected enum Mouse {HOVER, CLICKED}

    private ArrayList<GuiListener> listeners;
    private int width, height;
    private JPanel[][] cellArray;
    private boolean isCreated = false;
    private JLabel whitePieceLabel;
    private JLabel blackPieceLabel;
    private BufferedImage blackPiece, whitePiece, blackKingPiece,whiteKingPiece;
    private JPanel boardBackground;
    private Board board;

    public StandaardGui() {
        width = 500;
        height = 500;
        listeners = new ArrayList<GuiListener>();
        initializeJFrame();
    }

    @Override
    public void showBoard(Board board) {
        this.board = board;
        if (!isCreated) {
            loadImages();
            createBackground();
            createBoard();
            isCreated = true;
        }
        refreshBoard();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    private void initializeJFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Dammen");
        //Set the frame size, relative to the screen size.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);
        setResizable(false);
    }

    private void loadImages() {
        // Load images of pieces
        whitePiece = null;
        blackPiece = null;
        blackKingPiece = null;
        whiteKingPiece = null;

        try {
            whitePiece = ImageIO.read(new File("images\\krem_.png"));
            blackPiece = ImageIO.read(new File("images\\sedefli_mavi.png"));
            whiteKingPiece = ImageIO.read(new File("images\\krem_King.png"));
            blackKingPiece = ImageIO.read(new File("images\\sedefli_mavi_King.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createBackground() {
        cellArray = new JPanel[board.getSize()][board.getSize()];
        boardBackground = new JPanel();
        boardBackground.setLayout(new GridLayout(board.getSize(), board.getSize()));
    }

    private void createBoard() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                JPanel cell = new JPanel();
                cellArray[i][j] = cell;
                final int row = i;
                final int column = j;

                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        cell.setBackground(Color.WHITE);
                        boardBackground.add(cell);
                    } else {
                        cell.setBackground(Color.LIGHT_GRAY);
                        boardBackground.add(cell);
                        addMouseActions(cell, row, column);

                    }
                } else {
                    if (j % 2 == 0) {
                        cell.setBackground(Color.LIGHT_GRAY);
                        boardBackground.add(cell);
                        addMouseActions(cell, row, column);
                    } else {
                        cell.setBackground(Color.WHITE);
                        boardBackground.add(cell);
                    }
                }
            }
        }
    }

    private void refreshBoard() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                JPanel cell = cellArray[i][j];
                if (board.getPieces()[i][j].getStatus() == TypePiece.Status.EMPTY) {
                    cell.removeAll();
                }
                else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK && board.getPieces()[i][j].getRank() == TypePiece.Rank.KING){
                    ImageIcon blackPieceImageIcon = new ImageIcon(blackKingPiece.getScaledInstance(width / 13, width / 13, width / 13));
                    blackPieceLabel = new JLabel(blackPieceImageIcon);
                    cell.add(blackPieceLabel);
                }else if(board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE && board.getPieces()[i][j].getRank() == TypePiece.Rank.KING){
                    ImageIcon whitePieceImageIcon = new ImageIcon(whiteKingPiece.getScaledInstance(width / 13, width / 13, width / 13));
                    whitePieceLabel = new JLabel(whitePieceImageIcon);
                    cell.add(whitePieceLabel);
                }
                else if (board.getPieces()[i][j].getStatus() == TypePiece.Status.BLACK) {
                    // the width is divided by 13 to make the piece fit in the cell
                    ImageIcon blackPieceImageIcon = new ImageIcon(blackPiece.getScaledInstance(width / 13, width / 13, width / 13));
                    blackPieceLabel = new JLabel(blackPieceImageIcon);
                    cell.add(blackPieceLabel);
                } else if (board.getPieces()[i][j].getStatus() == TypePiece.Status.WHITE) {
                    // the width is divided by 13 to make the piece fit in the cell
                    ImageIcon whitePieceImageIcon = new ImageIcon(whitePiece.getScaledInstance(width / 13, width / 13, width / 13));
                    whitePieceLabel = new JLabel(whitePieceImageIcon);
                    cell.add(whitePieceLabel);
                }

            }
        }
        this.add(boardBackground);
        revalidate();
        repaint();
    }

    @Override
    public void changeColor(Piece piece,Color color){
        cellArray[piece.getRow()][piece.getColumn()].setBackground(color);
    }

    private void addMouseActions(JPanel cell, final int row, final int column) {
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                notifyListeners(Mouse.CLICKED, row, column);
            }
        });
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                notifyListeners(Mouse.HOVER, row, column);
            }
        });
    }

    public void addListeners(GuiListener newListener) {
        listeners.add(newListener);
    }

    @Override
    public void notifyListeners() {

    }

    private void notifyListeners(Mouse mouseType, int row, int column) {
        for (GuiListener listener : listeners) {
            if (mouseType == Mouse.HOVER)
                listener.hover(row, column);
            else if (mouseType == Mouse.CLICKED)
                listener.clicked(row, column);
        }
    }
}
