package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

public class VideoFlipperChild extends FlipperChildView {

    private static VideoPlayer videoPlayer;

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
        ViewGroup viewGroup;

        if (videoPlayer == null) {
            viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout, this, false);
            videoPlayer = (VideoPlayer) viewGroup.findViewById(R.id.content_screen_video_field);
        } else {
            viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout_without_player, this, false);
            RelativeLayout videoFrame = (RelativeLayout) viewGroup.findViewById(R.id.content_screen_video_frame);
            videoFrame.addView(videoPlayer);
        }

        this.addView(viewGroup);
        titleView = (TextView) this.findViewById(R.id.video_title);
        descriptionView = (TextView) this.findViewById(R.id.video_descr);
    }

    private void initVideoComponent() {
        titleView.setText(domainActivityData.name);
        descriptionView.setText(domainActivityData.notes);
    }

    @Override
    public void onBeforeFlippingOut() {
        if (videoPlayer != null) {
            videoPlayer.pause();
            videoPlayer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAfterFlippingIn() {
        if (videoPlayer != null) {
            videoPlayer.resetWithActivity(domainActivityData);
            videoPlayer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
    }

    public void resetDomainActivityData(DomainActivityData domainActivity) {
        domainActivityData = domainActivity;
        titleView.setText(domainActivity.name);
        descriptionView.setText(domainActivity.notes);
    }
}
