package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

public class VideoFlipperChild extends FlipperChildView {
    private VideoPlayer videoPlayer;
    private DomainActivityData domainActivityData;
    private TextView titleView;
    private TextView descriptionView;

    public VideoFlipperChild(Context context, DomainActivityData domainActivity) {
        super(context);
        this.domainActivityData = domainActivity;
        initUI();
        initVideoComponent();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout, this, false);
        this.addView(viewGroup);
        videoPlayer = (VideoPlayer) findViewById(R.id.content_screen_video_field);
        titleView = (TextView) this.findViewById(R.id.video_title);
        descriptionView = (TextView) this.findViewById(R.id.video_descr);
        videoPlayer.setVisibility(View.INVISIBLE);
    }

    private void initVideoComponent() {
        videoPlayer.setVideoData(domainActivityData);
        titleView.setText(domainActivityData.name);
        descriptionView.setText(domainActivityData.notes);
    }

    @Override
    public void onBeforeFlippingOut() {
        videoPlayer.pause();
        videoPlayer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAfterFlippingIn() {
        videoPlayer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        videoPlayer.onDestroy();
    }

    public void resetDomainActivityData(DomainActivityData domainActivity) {
        titleView.setText(domainActivity.name);
        descriptionView.setText(domainActivity.notes);
        videoPlayer.resetWithActivity(domainActivity);
    }
}
