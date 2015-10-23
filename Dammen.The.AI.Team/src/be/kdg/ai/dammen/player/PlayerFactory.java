package be.kdg.ai.dammen.player;

import be.kdg.ai.dammen.board.Board;
import be.kdg.ai.dammen.board.BoardListener;
import be.kdg.ai.dammen.piece.TypePiece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sliman on 1-10-2015.
 */
public class PlayerFactory  {
    private Player player;
    private List<PlayerListener> listeners;

    public PlayerFactory() {
        this.listeners = new ArrayList<PlayerListener>();
    }

    public void addListeners(PlayerListener newListener){
        listeners.add(newListener);
    }

    private void notifyListeners(){
        for (PlayerListener listener: listeners){
            listener.isNewPlayer(player);
        }
    }

    public void createPlayers(){
        player = new Player("Player", TypePiece.Status.WHITE);
        notifyListeners();
        player = new Player("COM", TypePiece.Status.BLACK);
        notifyListeners();
    }
}
