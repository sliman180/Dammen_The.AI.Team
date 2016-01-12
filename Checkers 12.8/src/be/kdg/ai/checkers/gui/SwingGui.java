package be.kdg.ai.checkers.gui;

import be.kdg.ai.checkers.domain.BoardState;
import be.kdg.ai.checkers.domain.Piece;
import be.kdg.ai.checkers.domain.board.ForceAttack;
import be.kdg.ai.checkers.domain.board.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents the game in a swing form.
 */
public class SwingGui extends JFrame implements Gui {

    protected enum Mouse {HOVER, CLICKED}

    private final ArrayList<GuiListener> listeners;
    private int width, height;
    private JPanel[][] cellArray;
    private boolean isCreated = false;
    private BufferedImage blackPiece, whitePiece, blackKingPiece, whiteKingPiece;
    private JPanel boardBackground;
    private BoardState boardState;
    private int boardSize;

    public SwingGui() {
        width = 600;
        height = 600;
        listeners = new ArrayList<>();
        initializeJFrame();
    }

    @Override
    public void showBoard(BoardState boardState) {
        this.boardState = boardState;
        boardSize = boardState.getBoard().getSize();
        if (!isCreated) {
            loadImages();
            createBackground();
            createBoard();
            isCreated = true;
        }
        refreshBoard();
    }


    private void initializeJFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
            whitePiece = ImageIO.read(new File("images/krem_.png"));
            blackPiece = ImageIO.read(new File("images/sedefli_mavi.png"));
            whiteKingPiece = ImageIO.read(new File("images/krem_King.png"));
            blackKingPiece = ImageIO.read(new File("images/sedefli_mavi_King.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createBackground() {
        cellArray = new JPanel[boardSize][boardSize];
        boardBackground = new JPanel();
        boardBackground.setLayout(new GridLayout(boardSize, boardSize));
    }

    private void createBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {

                JPanel cell = new JPanel();
                cellArray[row][column] = cell;

                if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                    cell.setBackground(Color.LIGHT_GRAY);
                    addMouseActions(cell, row, column);
                } else {
                    cell.setBackground(Color.WHITE);
                }
                boardBackground.add(cell);
            }
        }
    }


    private void refreshBoard() {
        changeColorAfterMustAttack(Color.LIGHT_GRAY);
        final int IMAGE_SIZE = (int) Math.round(width / (boardSize * 1.3));

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JPanel cell = cellArray[i][j];
                JLabel whitePieceLabel;
                JLabel blackPieceLabel;
                if (boardState.getBoard().getPieces()[i][j] == Piece.EMPTY) {
                    cell.removeAll();
                } else if (boardState.getBoard().getPieces()[i][j] == Piece.BLACK_KING) {
                    ImageIcon blackPieceImageIcon = new ImageIcon(blackKingPiece.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE));
                    blackPieceLabel = new JLabel(blackPieceImageIcon);
                    cell.add(blackPieceLabel);
                } else if (boardState.getBoard().getPieces()[i][j] == Piece.WHITE_KING) {
                    ImageIcon whitePieceImageIcon = new ImageIcon(whiteKingPiece.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE));
                    whitePieceLabel = new JLabel(whitePieceImageIcon);
                    cell.add(whitePieceLabel);
                } else if (boardState.getBoard().getPieces()[i][j] == Piece.BLACK) {
                    // the width is divided by 13 to make the piece fit in the cell
                    ImageIcon blackPieceImageIcon = new ImageIcon(blackPiece.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE));
                    blackPieceLabel = new JLabel(blackPieceImageIcon);
                    cell.add(blackPieceLabel);
                } else if (boardState.getBoard().getPieces()[i][j] == Piece.WHITE) {
                    // the width is divided by 13 to make the piece fit in the cell
                    ImageIcon whitePieceImageIcon = new ImageIcon(whitePiece.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE));
                    whitePieceLabel = new JLabel(whitePieceImageIcon);
                    cell.add(whitePieceLabel);
                }

            }
        }
        this.add(boardBackground);
        changeColorMustAttack(Color.green);
        revalidate();
        repaint();
    }

    private void changeColorMustAttack(Color color){
        ArrayList<ForceAttack> oldForcedattacks = boardState.getOldforcedToAttackPieces();
        for (ForceAttack fa : boardState.getForcedToAttackPieces()){
            cellArray[fa.getCurrentPosition().getRow()][fa.getCurrentPosition().getColumn()].setBackground(color);
            oldForcedattacks.add(fa);
        }
        boardState.setOldforcedToAttackPieces(oldForcedattacks);
    }

    private void changeColorAfterMustAttack(Color color){
        for (ForceAttack fa : boardState.getOldforcedToAttackPieces()){
            cellArray[fa.getCurrentPosition().getRow()][fa.getCurrentPosition().getColumn()].setBackground(color);
        }
    }

    @Override
    public void changeColor(Position position, Color color) {
        cellArray[position.getRow()][position.getColumn()].setBackground(color);
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
    public void showMessage(String gameWonMessage) {
        JOptionPane.showMessageDialog(this, gameWonMessage);
    }

    private void notifyListeners(Mouse mouseType, int row, int column) {
        for (GuiListener listener : listeners) {
            if (mouseType == Mouse.CLICKED)
                listener.clicked(row, column);
        }
    }
}


