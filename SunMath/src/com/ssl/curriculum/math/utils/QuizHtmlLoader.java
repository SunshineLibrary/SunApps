package com.ssl.curriculum.math.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuizHtmlLoader {
    private static final String QUIZ_TEMPLATE_ASSET_FILE_NAME = "sample-asciimath.html";

    private static QuizHtmlLoader loader;

    private String htmlString;

    private QuizHtmlLoader(Context context, String templateAssetFileName) {
        htmlString = "";
        loadTemplate(context, templateAssetFileName);
    }

    private void loadTemplate(Context context, String templateAssetFileName) {
        try {
            InputStream is = context.getAssets().open(templateAssetFileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                htmlString += line;
            }
            System.out.println("-----------htmlString = " + htmlString);

        } catch (IOException e) {
            Log.e("QuizHtml", "can not load quiz html template, " + QUIZ_TEMPLATE_ASSET_FILE_NAME);
        }
    }

    public static QuizHtmlLoader getInstance(Context context) {
        if (loader == null) {
            loader = new QuizHtmlLoader(context, QUIZ_TEMPLATE_ASSET_FILE_NAME);
        }
        return loader;
    }

    public String loadQuizHtmlWithNewContent(String newContent) {
        return QuizTemplateResolver.replaceWithNewContent(htmlString, newContent);
    }

}
