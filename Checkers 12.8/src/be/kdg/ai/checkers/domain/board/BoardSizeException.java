package be.kdg.ai.checkers.domain.board;

/**
 * This exception triggers when the size of the board is invalid.
 */
public class BoardSizeException extends RuntimeException {
    private final String errorMessage;

    public BoardSizeException(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
