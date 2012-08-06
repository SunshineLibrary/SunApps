package com.ssl.curriculum.math.component.flipperchildren;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.videoview.VideoPlayer;
import com.ssl.curriculum.math.model.activity.VideoDomainActivityData;

public class VideoFlipperChild extends LinearLayout {
    private VideoPlayer videoPlayer;
    private Context context;
    private VideoDomainActivityData domainActivityData;
    private TextView title;
    private TextView description;

    public VideoFlipperChild(Context context, AttributeSet attrs, VideoDomainActivityData activityData) {
        super(context, attrs);
        this.context = context;
        this.domainActivityData = activityData;
        initUI();
        initVideoComponent();
    }

    private void initUI() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.video_flip_layout, this, false);
        this.addView(viewGroup);
        videoPlayer = (VideoPlayer) findViewById(R.id.content_screen_video_field);
        title = (TextView) this.findViewById(R.id.video_title);
        description = (TextView) this.findViewById(R.id.video_descr);
    }

    private void initVideoComponent() {
        Uri video = Uri.parse("android.resource://" + this.context.getPackageName() + "/" + R.raw.speaking);
        videoPlayer.setVideoURI(video);
        title.setText(domainActivityData.getTitle());
        description.setText(domainActivityData.getDescription());
    }

}
