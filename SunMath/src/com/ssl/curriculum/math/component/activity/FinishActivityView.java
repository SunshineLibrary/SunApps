package com.ssl.curriculum.math.component.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import com.ssl.metadata.provider.MetadataContract;


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
        updateSectionStatus(mActivityViewer.getSectionId());
        mActivityViewer.flipCurrentViewOutToRight();
        mActivityViewer.finish();
       
        //send the broadcast
        //Intent intent = new Intent("com.liucong.start");
        //context.sendBroadcast(intent);
	}

    private void updateSectionStatus(int id) {
        ContentValues values = new ContentValues();
        values.put(MetadataContract.Sections._STATUS, "done");
        getContext().getContentResolver().update(MetadataContract.Sections.getSectionUri(id), values, null, null);
    }
}
