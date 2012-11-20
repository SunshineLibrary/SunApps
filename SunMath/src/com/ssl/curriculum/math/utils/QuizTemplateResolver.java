package com.ssl.curriculum.math.utils;

public class QuizTemplateResolver {

    public  static String replaceWithNewContent(String rawHtmlString, String newContent) {
    	String imageStr = "这是个简单的测试，包含图片<IMG src=\"file:///android_asset/ladder-shaped.jpg\"><br>";
    	String audioStr1 = "点击我<input type=\"button\" onclick=\"play()\" value=\"播放\" />开始播放";
    	String audioStr2 = "点击我<input type=\"button\" onclick=\"pause()\" value=\"暂停\" />暂停播放";
    	String audioStr = audioStr1 + "<br>" + audioStr2;
    	//add image or audio or video ,as so on...
        return rawHtmlString.replaceAll("<body>(.*?)</body>", "<body>" + newContent + imageStr + audioStr + "</body>");
    }
}
