package com.ssl.support.utils;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";

    public static boolean isEmpty(String str) {
        return str == null || str.equals(EMPTY_STRING);
    }
}
