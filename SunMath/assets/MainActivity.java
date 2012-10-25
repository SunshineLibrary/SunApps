package com.example.testmathdisplay;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;

@SuppressLint("SetJavaScriptEnabled") public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WebView wb = (WebView) findViewById(R.id.webview);
        wb.getSettings().setJavaScriptEnabled(true);  
        wb.loadUrl("file:///android_asset/test.html");
        //wb.loadUrl("file:///android_asset/MathJax/test/index.html");
        //wb.loadUrl("file:///android_asset/MathJax/test/question_body_template.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
