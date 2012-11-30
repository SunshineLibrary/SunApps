package com.ssl.curriculum.math.utils;

public class QuizTemplateResolver {

    public static String replaceWithNewContent(String rawHtmlString, String newContent){
    	return rawHtmlString.replaceAll("<body>(.*?)</body>", "<body>" + newContent + "</body>");
    }
}
