package com.epam.cdp.core.utils;

/**
 * @author Geniy00
 */
public class LoggingUtils {

    public static String toOneLine(final String message) {
        return message.replaceAll("\n", "").replaceAll("\t", "");
    }
}
