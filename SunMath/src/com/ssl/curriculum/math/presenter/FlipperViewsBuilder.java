package com.ssl.curriculum.math.presenter;

import android.content.Context;
import android.view.View;
import com.ssl.curriculum.math.component.flipperchildren.GalleryThumbnailPageFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.QuizFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.VideoFlipperChild;
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

    public View buildViewToFlipper(DomainActivityData domainActivity) {
        switch (domainActivity.type) {
            case TYPE_VIDEO:
                return new VideoFlipperChild(this.context, domainActivity);
            case TYPE_QUIZ:
                return new QuizFlipperChild(this.context, (QuizDomainData) domainActivity);
            case TYPE_GALLERY:
                GalleryThumbnailPageFlipperChild galleryThumbnailPage = new GalleryThumbnailPageFlipperChild(this.context, domainActivity);
                galleryThumbnailPage.setGalleryItemClickedListener(galleryThumbnailItemClickListener);
                return galleryThumbnailPage;
            default:
        }
        return null;
    }

    public void setGalleryThumbnailItemClickListener(GalleryItemClickedListener galleryThumbnailItemClickListener) {
        this.galleryThumbnailItemClickListener = galleryThumbnailItemClickListener;
    }
}
