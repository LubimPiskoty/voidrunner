package sk.piskotka.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    private static void Log(String tag, String tagColor, String text){
        String timeString = getTimeNow();
        String coloredTag = "[" +tagColor + tag + ANSI_RESET + "]";
        System.out.println("|" + timeString + "| " + coloredTag + " " + text);
    }

    public static void LogDebug(String text){
        Log("DEBUG", ANSI_WHITE_UNDERLINE, text);
    }
    
    public static void LogInfo(String text){
        Log("INFO", ANSI_CYAN, text);
    }
    
    public static void LogWarning(String text){
        Log("WARN", ANSI_YELLOW, text);
    }
    public static void LogError(String text){
        Log("ERROR", ANSI_RED, text);
    }
}
