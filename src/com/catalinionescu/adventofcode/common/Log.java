package com.catalinionescu.adventofcode.common;

public class Log {
    public static void logInfoMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            System.out.println(String.format("[INFO] %s", msg));
        } else {
            System.out.println(String.format("[INFO] %s", String.format(msg, args)));
        }
    }

    public static void logErrorMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            System.out.println(String.format("[ERROR] %s", msg));
        } else {
            System.out.println(String.format("[ERROR] %s", String.format(msg, args)));
        }
    }

    public static void logWarningMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            System.out.println(String.format("[WARN] %s", msg));
        } else {
            System.out.println(String.format("[WARN] %s", String.format(msg, args)));
        }
    }
}
