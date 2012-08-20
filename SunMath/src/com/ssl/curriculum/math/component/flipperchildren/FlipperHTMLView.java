package com.ssl.curriculum.math.component.flipperchildren;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.activity.MainActivity;
import com.ssl.curriculum.math.activity.WebViewActivity;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

/**
 * Created with IntelliJ IDEA.
 * User: mendlin
 * Date: 12-8-13
 * Time: 下午10:09
 * To change this template use File | Settings | File Templates.
 */
public class FlipperHTMLView extends FlipperChildView {

    public FlipperHTMLView(Context context, DomainActivityData data) {
        super(context);
        initUI();
        initComponents(context);
    }

    private void initComponents(final Context context) {
        Button button = (Button) findViewById(R.id.flipper_html_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, WebViewActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.flipper_html_layout, this, false);
        addView(viewGroup);
    }
}
