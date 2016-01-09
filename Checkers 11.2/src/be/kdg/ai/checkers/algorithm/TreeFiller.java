package be.kdg.ai.checkers.algorithm;

import be.kdg.ai.checkers.domain.BoardState;

/**
 * This class fills the tree that's going to be used in the {@link MinimaxAlgorithm} class.
 */
public interface TreeFiller {
    /**
     * Get the node where needed.
     * @return
     */
    BoardState getCurrentNode();

    /**
     * The method that fills the tree
     */
    void fillTree();
}
