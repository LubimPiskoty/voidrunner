package sk.piskotka.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import sk.piskotka.GameManager;

public class Logger {
    public static Logger instance;
    //TODO: Make this bools into arg params
    private boolean isDebug = true;
    private boolean logConsole = true;
    private boolean logFile = false;
    
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

    private Logger(){}

    public static Logger getInstance(){
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    private static String getTimeNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.now().format(formatter);
    }

    private static void log(String tag, String tagColor, Class<?> caller, String text){
        String timeString = getTimeNow();
        String coloredTag = "[" +tagColor + tag + ANSI_RESET + "]";
        String classString = ANSI_WHITE_UNDERLINE + caller.getSimpleName() + ANSI_RESET;
        String log = String.format("|%-8s| %-8s %s: %s", timeString, coloredTag, classString, text);
        System.out.println(log);
    }

    public static void logDebug(Class<?> caller, String text){
        if (GameManager.isDebug())
            log("DEBUG", ANSI_WHITE_UNDERLINE, caller, text);
    }
    
    public static void logInfo(Class<?> caller, String text){
        log("INFO", ANSI_CYAN, caller, text);
    }
    
    public static void logWarning(Class<?> caller, String text){
        log("WARN", ANSI_YELLOW, caller, text);
    }
    public static void logError(Class<?> caller, String text){
        log("ERROR", ANSI_RED, caller, text);
    }

    public static void throwError(Class<?> caller, String text){
        logError(caller, text);
        throw new Error(text);
    }
}
