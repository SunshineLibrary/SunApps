package com.ssl.curriculum.math.component.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;


/**
 * @author Linfeng Yang
 * @version 1.0
 */
public class FinishActivityView extends ActivityView implements View.OnClickListener {

	int activityId;
    ImageView finishButtonView;
	Context context;

    public FinishActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
        initComponents(context);        
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.section_finished_layout, this, false);
        addView(viewGroup);
    }

    private void initComponents(final Context context) {
    	this.context = context;
        finishButtonView = (ImageView) findViewById(R.id.section_finished_button);
        finishButtonView.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
        mActivityViewer.flipCurrentViewOutToRight();
        mActivityViewer.finish();
	}



	
}
