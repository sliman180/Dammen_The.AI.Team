package be.kdg.ai.dammen.player;

import be.kdg.ai.dammen.piece.TypePiece;

/**
 * Created by Sliman on 1-10-2015.
 */
public class Player {
    private String name;
    private TypePiece.Status status;

    public Player(String name, TypePiece.Status status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public TypePiece.Status getStatus() {
        return status;
    }
}
