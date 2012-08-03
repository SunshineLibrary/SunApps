package com.ssl.curriculum.math.presenter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.ssl.curriculum.math.activity.MainActivity;
import com.ssl.curriculum.math.component.flipperchildren.GalleryThumbnailPageFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.QuizFlipperChild;
import com.ssl.curriculum.math.component.flipperchildren.VideoFlipperChild;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.QuizDomainActivityData;
import com.ssl.curriculum.math.model.activity.VideoDomainActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;

import java.util.HashMap;

import static com.sunshine.metadata.provider.MetadataContract.Activities.*;

public class MainActivityPresenter {
    public static String FLIPPER = "view_flipper";

    private ActivityContentProvider activityProvider;
    private PageFlipListener flipListener;
    private HashMap<String, View> UIBindings = new HashMap<String, View>();
    public final MainActivity activity;

    public MainActivityPresenter(MainActivity activity) {
        this.activity = activity;
        this.activityProvider = new ActivityContentProvider(this.activity);
    }

    public void bindUIElement(String bindKey, View UIElement) {
        UIBindings.put(bindKey, UIElement);
    }

    public void setPageFlipListener(PageFlipListener pfl) {
        this.flipListener = pfl;
    }

    public ActivityContentProvider getActvityProvider() {
        return this.activityProvider;
    }

    public void present(DomainActivityData domainActivity, int mode) {
        final ViewFlipper viewFlipper = (ViewFlipper) getFlipper();
        if (viewFlipper == null)
            return;

        View activityView = null;

        switch (domainActivity.getType()) {
            case TYPE_VIDEO: {
                activityView = new VideoFlipperChild(this.activity, null, (VideoDomainActivityData) domainActivity);
            }
            break;
            case TYPE_TEXT: {
                activityView = new TextView(this.activity);
                ((TextView) activityView).setText("TextTypedObject - Damn");
            }
            break;
            case TYPE_AUDIO: {
                activityView = new TextView(this.activity);
                ((TextView) activityView).setText("Audio - Wahoo!");
            }
            break;
            case TYPE_HTML: {
                activityView = new TextView(this.activity);
                ((TextView) activityView).setText("HTML");
            }
            break;
            case TYPE_QUIZ: {
                activityView = new QuizFlipperChild(this.activity, null, (QuizDomainActivityData) domainActivity);
            }
            break;
            case TYPE_GALLERY: {
                GalleryThumbnailPageFlipperChild galleryThumbnailPage = new GalleryThumbnailPageFlipperChild(this.activity);
                galleryThumbnailPage.setGalleryItemClickedListener(this.activity.getGalleryThumbnailItemClickListener());
                activityView = galleryThumbnailPage;
            }
            break;
            default: {
                activityView = new TextView(this.activity);
                ((TextView) activityView).setText("You should never see this. Probably.");
            }
        }
        if (viewFlipper != null) {
            viewFlipper.addView(activityView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            if (mode == 1) {
                viewFlipper.showNext();
            } else if (mode == -1) {
                viewFlipper.showPrevious();
            }

            while (viewFlipper.getChildCount() > 1) {
                viewFlipper.removeViewAt(0);
            }
        }
    }

    public PageFlipListener getFlipListener(MainActivityPresenter self) {
        return self.flipListener;
    }

    private View getFlipper() {
        return this.UIBindings.get(FLIPPER);
    }

}
