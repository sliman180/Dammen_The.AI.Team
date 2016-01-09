package be.kdg.ai.checkers.logger;

/**
 * This class is responsible for logging warnings, errors, ... in the console.
 */
public class Logger {
    public enum LogType {
        DEBUG, INFO, WARNING, ERROR, FATAL
    }

    private static Logger instance = new Logger();
    private Logger(){}
    public static synchronized Logger getInstance(){return instance;}

    private String lastLogMessage;

    public void log(LogType type, String message)
    {
        lastLogMessage = String.format("%7s : %s", type.toString(), message);
        System.out.println(lastLogMessage);
    }

    public String getLastLogMessage() {
        return lastLogMessage;
    }

}
