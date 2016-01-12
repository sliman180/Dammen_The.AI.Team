package be.kdg.ai.checkers.domain;

import be.kdg.ai.checkers.domain.board.Board;
import be.kdg.ai.checkers.domain.board.ForceAttack;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

/**
 * This class represents the state of the board,
 * meaning it saves the the position of its pieces due to Board and the player
 */
public class BoardState extends DefaultMutableTreeNode {
    private Board board;
    private Player player;
    private ArrayList<ForceAttack> forcedToAttackPieces;
    private ArrayList<ForceAttack> oldforcedToAttackPieces;
    private double heuristicValue;

    public BoardState(Board board, Player player) {
        this.board = board;
        this.player = player;
        forcedToAttackPieces = new ArrayList<>();
        oldforcedToAttackPieces = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<ForceAttack> getForcedToAttackPieces() {
        return forcedToAttackPieces;
    }

    public void setForcedToAttackPieces(ArrayList<ForceAttack> forcedToAttackPieces) {
        this.forcedToAttackPieces = forcedToAttackPieces;
    }

    public void setOldforcedToAttackPieces(ArrayList<ForceAttack> oldforcedToAttackPieces) {
        this.oldforcedToAttackPieces = oldforcedToAttackPieces;
    }

    public ArrayList<ForceAttack> getOldforcedToAttackPieces() {
        return oldforcedToAttackPieces;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardState that = (BoardState) o;

        return getBoard().equals(that.getBoard()) && getPlayer().equals(that.getPlayer());

    }

    @Override
    public int hashCode() {
        int result = getBoard().hashCode();
        result = 31 * result + getPlayer().hashCode();
        return result;
    }
}
