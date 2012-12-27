package com.ssl.curriculum.math.component.quiz;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ssl.curriculum.math.component.viewer.QuizComponentViewer;
import com.ssl.curriculum.math.model.activity.quiz.QuizQuestion;
import com.ssl.curriculum.math.presenter.quiz.AudioJavascriptInterface;
import com.ssl.curriculum.math.utils.QuizHtmlLoader;
import com.ssl.metadata.provider.MetadataContract;

public abstract class QuizQuestionView extends QuizComponentView {

    private static final String ATTACHMENT_FORMAT = "<div style='text-align:center'><img src='%s'/></div>";

    protected WebView questionWebView;
    protected ProgressBar progressBar;
    protected TextView  questionTitle;

    public QuizQuestionView(Context context, QuizComponentViewer quizComponentViewer) {
        super(context, quizComponentViewer);
        initUI();
        initWebView();
    }

    protected abstract void initUI();

    protected void initWebView() {
        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.getSettings().setAllowFileAccess(true);
        questionWebView.getSettings().setDomStorageEnabled(true);
        questionWebView.setScrollBarStyle(0);

        questionWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    onLoadComplete();
                }
            }
        });
        
        //hereLiu
        questionWebView.addJavascriptInterface(new AudioJavascriptInterface(), "audioManager");
    }

    public abstract int getQuestionId();

    public abstract boolean onQuestionAnswered();

    public abstract void setQuestion(QuizQuestion question, int positionNum);

    protected abstract String getQuizContent();

    protected void resetView() {
        questionWebView.clearView();
        questionWebView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void onLoadComplete() {
        progressBar.setVisibility(View.INVISIBLE);
        questionWebView.setVisibility(View.VISIBLE);
    }

    public void onDestroy() {
        if (questionWebView != null) {
        	questionWebView.loadUrl("javascript:onDestory()");
            questionWebView.destroy();
        }
    }

    protected String getAttachmentContent() {
        Attachment attachment = getAttachment();
        if (attachment != null) {
            if (attachment.isImage()) {
                return String.format(ATTACHMENT_FORMAT, attachment.filePath);
            }
        }
        return "";
    }

    private Attachment getAttachment() {
        Attachment attachment = null;
        Cursor cursor = getContext().getContentResolver().query(MetadataContract.Files.CONTENT_URI, null,
                MetadataContract.Files._URI_PATH + "= ? ",
                new String[] {MetadataContract.Problems.getProblemUri(getQuestionId()).getPath()}, null);
        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MetadataContract.Files._FILE_PATH));
            attachment = new Attachment(path);
        }
        cursor.close();
        return attachment;
    }

    protected void loadQuizHtml(String quizContent, String questionNum) {
        questionNum = "<p style=\"font-weight:bold;\">"+questionNum+"</p>";
        quizContent = questionNum + quizContent;
        final String data = QuizHtmlLoader.getInstance(getContext()).loadQuestionBodyWithNewContent(quizContent);
        
      /*
       * Android thinks file:// schema insecure, so we use http:// here.
       * And for loadDataWithBaseUrl, the first parameter baseUrl has no exact meaning, we just use it
       * to tell Android we use the secure schema: http://
       * 
       *
       * */
        questionWebView.loadDataWithBaseURL("http://test", data, "text/html", "utf-8", null);
    }

    private static class Attachment {
        public String filePath;
        public String extension;

        public Attachment(String path) {
            filePath = "file://" + path;
            extension = filePath.substring(filePath.lastIndexOf('.'));
        }

        public boolean isImage() {
            return extension.equals(".jpeg") ||
                    extension.equals(".jpg") ||
                    extension.equals(".png") ||
                    extension.equals(".gif");
        }
    }
}
