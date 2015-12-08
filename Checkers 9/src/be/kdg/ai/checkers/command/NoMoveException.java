package be.kdg.ai.checkers.command;

/**
 * This exception-class will be triggered when the user tries to make an impossible move
 */
public class NoMoveException extends RuntimeException {
    private final String errorMessage;

    protected NoMoveException(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
