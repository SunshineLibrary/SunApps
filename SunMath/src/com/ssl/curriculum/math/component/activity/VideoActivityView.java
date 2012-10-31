package com.ssl.curriculum.math.component.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.LinkedActivityData;
import com.ssl.curriculum.math.component.viewer.ActivityViewer;

public class VideoActivityView extends ActivityView {

    private static VideoPlayer videoPlayer;

    private TextView tv_title;
    private TextView tv_description;

    public VideoActivityView(Context context, ActivityViewer activityViewer) {
        super(context, activityViewer);
        initUI();
    }

    @Override
    public void setActivity(LinkedActivityData activityData) {
        super.setActivity(activityData);
        setTitle(activityData.name);
        setDescription(activityData.notes);
        setVideoId(activityData.activityId);
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout, this, false);
        videoPlayer = (VideoPlayer) viewGroup.findViewById(R.id.content_screen_video_field);
        tv_title = (TextView) viewGroup.findViewById(R.id.video_title);
        tv_description = (TextView) viewGroup.findViewById(R.id.video_descr);
        this.addView(viewGroup);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setDescription(String description) {
        tv_description.setText(description);
    }

    private void setVideoId(int id) {
        videoPlayer.setVideoId(id);
        videoPlayer.reset();
    }

    @Override
    public void onAfterFlippingIn() {
        super.onAfterFlippingIn();
        videoPlayer.setVisibility(VISIBLE);

    }

    @Override
    public void onBeforeFlippingOut() {
        super.onBeforeFlippingOut();
        videoPlayer.pause();
        videoPlayer.setVisibility(GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayer.onDestroy();
    }
}

