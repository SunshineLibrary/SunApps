package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.VideoDomainActivityData;

public class VideoFlipperChild extends LinearLayout {
    private VideoPlayer videoPlayer;
    private VideoDomainActivityData domainActivityData;
    private TextView titleView;
    private TextView descriptionView;

    public VideoFlipperChild(Context context, VideoDomainActivityData domainActivity) {
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
        Uri video = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.speaking);
        videoPlayer.setVideoURI(video);
        titleView.setText(domainActivityData.getTitle());
        descriptionView.setText(domainActivityData.getDescription());
    }

    public void showPlayer() {
        videoPlayer.setVisibility(View.VISIBLE);
    }

    public void hidePlayer() {
        videoPlayer.pause();
        videoPlayer.setVisibility(View.INVISIBLE);
    }
}
