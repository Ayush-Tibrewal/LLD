// File: ChainOfResponsibilityLogger.java

public class ChainOfResponsibilityLogger {

    // ---- ENUM for Log Levels ----
    public enum LogLevel {
        INFO,
        DEBUG,
        ERROR
    }

    // ---- Base Abstract Class ----
    public static abstract class LogProcessor {

        private LogProcessor nextLoggerProcessor;

        public LogProcessor(LogProcessor nextLoggerProcessor) {
            this.nextLoggerProcessor = nextLoggerProcessor;
        }

        public void log(LogLevel logLevel, String message) {
            if (nextLoggerProcessor != null) {
                nextLoggerProcessor.log(logLevel, message);
            }
        }
    }

    // ---- INFO Logger ----
    public static class InfoLogProcessor extends LogProcessor {
        public InfoLogProcessor(LogProcessor nextLogProcessor) {
            super(nextLogProcessor);
        }

        @Override
        public void log(LogLevel logLevel, String message) {
            if (logLevel == LogLevel.INFO) {
                System.out.println("INFO: " + message);
            } else {
                super.log(logLevel, message);
            }
        }
    }

    // ---- DEBUG Logger ----
    public static class DebugLogProcessor extends LogProcessor {
        public DebugLogProcessor(LogProcessor nextLogProcessor) {
            super(nextLogProcessor);
        }

        @Override
        public void log(LogLevel logLevel, String message) {
            if (logLevel == LogLevel.DEBUG) {
                System.out.println("DEBUG: " + message);
            } else {
                super.log(logLevel, message);
            }
        }
    }

    // ---- ERROR Logger ----
    public static class ErrorLogProcessor extends LogProcessor {
        public ErrorLogProcessor(LogProcessor nextLogProcessor) {
            super(nextLogProcessor);
        }

        @Override
        public void log(LogLevel logLevel, String message) {
            if (logLevel == LogLevel.ERROR) {
                System.out.println("ERROR: " + message);
            } else {
                super.log(logLevel, message);
            }
        }
    }

    // ---- MAIN Method ----
    public static void main(String[] args) {

        // Create the chain: Error -> Debug -> Info
        LogProcessor loggerChain = new ErrorLogProcessor(
                new DebugLogProcessor(
                        new InfoLogProcessor(null)
                )
        );

        // Test different log levels
        loggerChain.log(LogLevel.INFO, "This is an info message");
        loggerChain.log(LogLevel.DEBUG, "This is a debug message");
        loggerChain.log(LogLevel.ERROR, "This is an error message");
    }
}
