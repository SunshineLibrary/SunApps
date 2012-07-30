package com.ssl.curriculum.math.component.flipperchildren;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.ssl.curriculum.math.R;

public class QuizFillInFlipperChild extends LinearLayout {

    private WebView questionWebView;
    private ProgressDialog progressDialog;

    public QuizFillInFlipperChild(Context context) {
        super(context);
        initUI();
        initWebView();
        loadURL();
    }

    private void initUI() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.quiz_fill_in_flipper_child, this, false);
        addView(viewGroup);
        questionWebView = (WebView) findViewById(R.id.quiz_fill_in_flipper_child_question);
    }
    private void initWebView() {
        questionWebView.getSettings().setJavaScriptEnabled(true);
        questionWebView.setScrollBarStyle(0);
        questionWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        questionWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hideProgressBar();
                }
                super.onProgressChanged(view, newProgress);
            }


        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("数据载入中，请稍候！");
    }

    private void loadURL() {
        questionWebView.loadUrl("http://baidu.com");
        showProgressBar();
    }

    private void showProgressBar() {
        post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
    }

    private void hideProgressBar() {
        post(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
            }
        });

    }
}
