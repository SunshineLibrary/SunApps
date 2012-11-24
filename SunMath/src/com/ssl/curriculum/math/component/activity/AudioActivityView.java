package com.ssl.curriculum.math.component.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.audioview.AudioPlayer;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
/**
 * 
 * @author Cong Liu
 * Version 1.0
 */

public class AudioActivityView extends ActivityView {

    private static AudioPlayer audioPlayer;

    private TextView tv_title;
    private TextView tv_description;

    public AudioActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
    }

    @Override
    public void setActivity(LinkedActivityData activityData) {
        super.setActivity(activityData);
        setTitle(activityData.name);
        setDescription(activityData.notes);
        setAudioId(activityData.activityId);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.audio_flip_layout, this, false);
       audioPlayer = (AudioPlayer) viewGroup.findViewById(R.id.content_screen_audio_field);
        tv_title = (TextView) viewGroup.findViewById(R.id.audio_title);
        tv_description = (TextView) viewGroup.findViewById(R.id.audio_descr);
        this.addView(viewGroup);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setDescription(String description) {
        tv_description.setText(description);
    }

    private void setAudioId(int id) {
        audioPlayer.setAudioId(id);
        //audioPlayer.reset();
    }

    @Override
    public void onAfterFlippingIn() {
        super.onAfterFlippingIn();
        audioPlayer.setVisibility(VISIBLE);
    }

    @Override
    public void onBeforeFlippingOut() {
        super.onBeforeFlippingOut();
        audioPlayer.pause();
        audioPlayer.setVisibility(GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioPlayer.onDestroy();
    }
}

