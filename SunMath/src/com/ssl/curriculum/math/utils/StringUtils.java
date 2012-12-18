package com.ssl.curriculum.math.utils;

public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || string == "";
    }
    
    /**
     * transcoding the special char to fit html style
     * 
     * @param string
     * @return
     */
    public static String Transcoding2Html(String string) {
    	string = string.replaceAll("&", "&amp;");
    	string = string.replaceAll("<", "&lt;");
    	string = string.replaceAll(">", "&gt;");
    	string = string.replaceAll("\"", "&quot;");
    	string = string.replaceAll(" ", "&nbsp;");
    	return string;
    }
}
