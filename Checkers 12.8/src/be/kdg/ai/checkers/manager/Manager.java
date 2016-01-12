package be.kdg.ai.checkers.manager;

import be.kdg.ai.checkers.algorithm.Algorithm;
import be.kdg.ai.checkers.command.IMoveCommand;
import be.kdg.ai.checkers.controller.Controller;
import be.kdg.ai.checkers.domain.board.Board;
import be.kdg.ai.checkers.domain.Player;

/**
 * Separates workload
 */
interface Manager {
    void start(Player player1, Board board);
    void start(Player player, int boardSize);

    IMoveCommand getMoveCommand();
    void setMoveCommand(IMoveCommand moveCommand);
    Controller getController();
    void setController(Controller controller);
    Algorithm getAlgorithm();
    void setAlgorithm(Algorithm algorithm);
}
