package com.ssl.curriculum.math.utils;

import android.content.Context;
import android.util.Log;
import com.ssl.curriculum.math.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuizHtmlLoader {

    private static QuizHtmlLoader loader;

    private String htmlBodyString;
    private String htmlChoiceString;

    private QuizHtmlLoader(Context context) {
        htmlBodyString = generateTemplateString(context, "question_template.html");
        htmlChoiceString = generateTemplateString(context, "question_choice_template.html");
    }

    private String generateTemplateString(Context context, String templateAssetFileName) {
        String htmlString = "";
        try {
            InputStream is = context.getAssets().open(templateAssetFileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                htmlString += line;
            }
        } catch (IOException e) {
            Log.e("QuizHtml", "can not load quiz html template, " + templateAssetFileName);
        }
        return htmlString;
    }

    public static QuizHtmlLoader getInstance(Context context) {
        if (null == loader) {
            loader = new QuizHtmlLoader(context);
        }
        return loader;
    }

    public String loadQuestionBodyWithNewContent(String newContent, int positionNum) {
    	//hereLiu:
        return QuizTemplateResolver.replaceWithNewContent(htmlBodyString, newContent, positionNum);
    }

    public String loadQuestionChoiceWithNewContent(String newContent) {
        return QuizTemplateResolver.replaceWithNewContent(htmlChoiceString, newContent);
    }

}
