package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.ssl.curriculum.math.R;

/**
 * Created with IntelliJ IDEA.
 * User: mendlin
 * Date: 12-8-14
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class WebViewActivity extends Activity {
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        webView = (WebView) findViewById(R.id.webview_main);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://42.121.65.247");
    }
}
