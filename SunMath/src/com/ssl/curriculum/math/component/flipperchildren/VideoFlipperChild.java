package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.DomainActivityData;

import java.util.zip.Inflater;

public class VideoFlipperChild extends FlipperChildView {

    private static VideoPlayer videoPlayer;
    private RelativeLayout videoFrame;

    private DomainActivityData domainActivityData;
    private TextView titleView;
    private TextView descriptionView;

    private Handler mHandler;

    public VideoFlipperChild(Context context, DomainActivityData domainActivity) {
        super(context);
        this.domainActivityData = domainActivity;
        mHandler = new Handler();
        initUI();
        initVideoComponent();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup;

        if (videoPlayer == null) {
            viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout, this, false);
            videoFrame = (RelativeLayout) viewGroup.findViewById(R.id.content_screen_video_frame);
            videoPlayer = (VideoPlayer) viewGroup.findViewById(R.id.content_screen_video_field);
            videoFrame.removeView(videoPlayer);
        } else {
            viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout_without_player, this, false);
            videoFrame = (RelativeLayout) viewGroup.findViewById(R.id.content_screen_video_frame);
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
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    videoFrame.removeView(videoPlayer);
                }
            });
        }
    }

    @Override
    public void onAfterFlippingIn() {
        if (videoPlayer != null) {
            videoPlayer.setVideoData(domainActivityData);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    videoFrame.addView(videoPlayer);
                    videoPlayer.reset();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
    }
}

