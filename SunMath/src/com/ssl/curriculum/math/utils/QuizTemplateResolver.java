package com.ssl.curriculum.math.utils;

public class QuizTemplateResolver {

    public  static String replaceWithNewContent(String rawHtmlString, String newContent,int positionNum) {
    	String imageStr = "这是个简单的测试，包含图片<IMG src=\"file:///android_asset/ladder-shaped.jpg\"><br>";
    	String audioStr1 = "点击我<input type=\"button\" onclick=\"play()\" value=\"播放\" />开始播放";
    	String audioStr2 = "点击我<input type=\"button\" onclick=\"pause()\" value=\"暂停\" />暂停播放";
    	String audioStr = audioStr1 + "<br>" + audioStr2;
    	//hereLiu
    	String Question_num = "<p style=\"font-size:30px; font-weight:bold;\">第"+positionNum+"题</p>";
    	//add image or audio or video ,as so on...
        return rawHtmlString.replaceAll("<body>(.*?)</body>", "<body>" + Question_num + newContent + imageStr + audioStr + "</body>");
    }
    
    public static String replaceWithNewContent(String rawHtmlString, String newContent){
    	return rawHtmlString.replaceAll("<body>(.*?)</body>", "<body>" + newContent + "</body>");
    }
}
