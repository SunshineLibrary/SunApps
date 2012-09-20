package com.ssl.curriculum.math.component.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.activity.WebViewActivity;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;

/**
 * Created with IntelliJ IDEA.
 * User: mendlin
 * Date: 12-8-13
 * Time: 下午10:09
 * To change this template use File | Settings | File Templates.
 */
public class HtmlActivityView extends ActivityView {

    public HtmlActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
        initComponents(context);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.flipper_html_layout, this, false);
        addView(viewGroup);
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

}
