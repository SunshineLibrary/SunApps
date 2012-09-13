package com.ssl.curriculum.math.presenter;

import android.content.Context;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.component.flipperchildren.*;
import com.ssl.curriculum.math.listener.GalleryItemClickedListener;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.QuizDomainData;

import static com.sunshine.metadata.provider.MetadataContract.Activities.*;

public class FlipperViewsBuilder {
    private Context context;
    private GalleryItemClickedListener galleryThumbnailItemClickListener;

    public FlipperViewsBuilder(Context context) {
        this.context = context;
    }

    public boolean buildViewToFlipper(ViewFlipper viewFlipper, DomainActivityData domainActivity) {
        switch (domainActivity.type) {
            case TYPE_VIDEO:
                viewFlipper.addView(new VideoFlipperChild(this.context, domainActivity));
                return true;
            case TYPE_QUIZ:
                viewFlipper.addView(new QuizFlipperChild(this.context, (QuizDomainData) domainActivity));
                return true;
            case TYPE_GALLERY:
                GalleryThumbnailPageFlipperChild galleryThumbnailPage = new GalleryThumbnailPageFlipperChild(this.context, domainActivity);
                galleryThumbnailPage.setGalleryItemClickedListener(galleryThumbnailItemClickListener);
                viewFlipper.addView(galleryThumbnailPage);
                return true;
            case TYPE_TEXT:
                viewFlipper.addView(new FlipperTextView(this.context, domainActivity));
                return true;
            case TYPE_HTML:
                viewFlipper.addView(new FlipperHTMLView(this.context, domainActivity));
                return true;
            default:
        }
        return false;
    }

    public void setGalleryThumbnailItemClickListener(GalleryItemClickedListener galleryThumbnailItemClickListener) {
        this.galleryThumbnailItemClickListener = galleryThumbnailItemClickListener;
    }
}
