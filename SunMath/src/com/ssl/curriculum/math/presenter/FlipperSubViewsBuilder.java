package com.ssl.curriculum.math.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.component.flipperchildren.GalleryThumbnailPageFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.QuizFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.VideoFlipperChild;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.QuizDomainActivityData;
import com.ssl.curriculum.math.model.activity.VideoDomainActivityData;

import static com.sunshine.metadata.provider.MetadataContract.Activities.*;

public class FlipperSubViewsBuilder {
    private Context context;
    private ViewFlipper viewFlipper;
    private GalleryItemClickedListener galleryThumbnailItemClickListener;


    public FlipperSubViewsBuilder(Context context, ViewFlipper viewFlipper) {
        this.context = context;
        this.viewFlipper = viewFlipper;
    }

    public void buildViewToFlipper(DomainActivityData domainActivity) {
        View view;
        switch (domainActivity.type) {
            case TYPE_VIDEO:
                view = new VideoFlipperChild(this.context, null, (VideoDomainActivityData) domainActivity);
                break;
            case TYPE_TEXT:
                view = new TextView(this.context);
                ((TextView) view).setText("TextTypedObject - Damn");
                break;
            case TYPE_AUDIO:
                view = new TextView(this.context);
                ((TextView) view).setText("Audio - Wahoo!");
                break;
            case TYPE_HTML:
                view = new TextView(this.context);
                ((TextView) view).setText("HTML");
                break;
            case TYPE_QUIZ:
                view = new QuizFlipperChild(this.context, null, (QuizDomainActivityData) domainActivity);
                break;
            case TYPE_GALLERY:
                GalleryThumbnailPageFlipperChild galleryThumbnailPage = new GalleryThumbnailPageFlipperChild(this.context);
                galleryThumbnailPage.setGalleryItemClickedListener(galleryThumbnailItemClickListener);
                view = galleryThumbnailPage;
                break;
            default:
                view = new TextView(this.context);
                ((TextView) view).setText("You should never see this. Probably.");
        }
        viewFlipper.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    public void setGalleryThumbnailItemClickListener(GalleryItemClickedListener galleryThumbnailItemClickListener) {
        this.galleryThumbnailItemClickListener = galleryThumbnailItemClickListener;
    }
}
