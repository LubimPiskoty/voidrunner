package sk.piskotka.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import sk.piskotka.GameManager;

/**
 * Logger class for managing application logs with support for console and file logging.
 * Provides methods for logging messages with different severity levels.
 */
@SuppressWarnings("unused")
public class Logger {
    public static Logger instance;
    //TODO: Make this bools into arg params
    private final boolean isDebug = true;
    private final boolean logConsole = true;
    private final boolean logFile = false;
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_WHITE_UNDERLINE = "\u001B[4;37m";  // WHITE

    /**
     * Private constructor to enforce singleton pattern.
     */
    private Logger(){}

    /**
     * Returns the singleton instance of the Logger.
     * 
     * @return the Logger instance
     */
    public static Logger getInstance(){
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    /**
     * Gets the current time formatted as HH:mm:ss.
     * 
     * @return the current time as a string
     */
    private static String getTimeNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.now().format(formatter);
    }

    /**
     * Logs a message with the specified tag, color, caller class, and text.
     * 
     * @param tag the log tag (e.g., DEBUG, INFO)
     * @param tagColor the color associated with the tag
     * @param caller the class that is logging the message
     * @param text the log message
     */
    private static void log(String tag, String tagColor, Class<?> caller, String text){
        String timeString = getTimeNow();
        String coloredTag = "[" +tagColor + tag + ANSI_RESET + "]";
        String classString = ANSI_WHITE_UNDERLINE + caller.getSimpleName() + ANSI_RESET;
        String log = String.format("|%-8s| %-8s %s: %s", timeString, coloredTag, classString, text);
        System.out.println(log);
    }

    /**
     * Logs a debug message if debug mode is enabled.
     * 
     * @param caller the class that is logging the message
     * @param text the debug message
     */
    public static void logDebug(Class<?> caller, String text){
        if (GameManager.isDebug())
            log("DEBUG", ANSI_WHITE_UNDERLINE, caller, text);
    }
    
    /**
     * Logs an informational message.
     * 
     * @param caller the class that is logging the message
     * @param text the informational message
     */
    public static void logInfo(Class<?> caller, String text){
        log("INFO", ANSI_CYAN, caller, text);
    }
    
    /**
     * Logs a warning message.
     * 
     * @param caller the class that is logging the message
     * @param text the warning message
     */
    public static void logWarning(Class<?> caller, String text){
        log("WARN", ANSI_YELLOW, caller, text);
    }

    /**
     * Logs an error message.
     * 
     * @param caller the class that is logging the message
     * @param text the error message
     */
    public static void logError(Class<?> caller, String text){
        log("ERROR", ANSI_RED, caller, text);
    }

    /**
     * Logs an error message and throws an Error with the specified text.
     * 
     * @param caller the class that is logging the message
     * @param text the error message
     * @throws Error with the specified text
     */
    public static void throwError(Class<?> caller, String text){
        logError(caller, text);
        throw new Error(text);
    }

    public boolean isLogConsole() {
        return logConsole;
    }
}
