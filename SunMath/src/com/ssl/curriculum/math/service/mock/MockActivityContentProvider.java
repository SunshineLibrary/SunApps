package com.ssl.curriculum.math.service.mock;


import android.content.Context;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.model.activity.VideoDomainActivityData;
import com.ssl.curriculum.math.service.ActivityContentProvider;

public class MockActivityContentProvider extends ActivityContentProvider {

    private Context context;

    public MockActivityContentProvider(Context context) {
        super(context);
    }

    @Override
    public DomainActivityData fetchActivityById(int activityId, int sectionId) {
        activityId = (int) Math.round(Math.random() * 100);
        if (activityId % 4 == 0) {
            VideoDomainActivityData vad = new VideoDomainActivityData();
            vad.initVideoMetadata("This is just a test" + activityId, "Blah", 1000);
            return vad;
        }
        if (activityId % 4 == 1 || activityId % 4 == 2) {
            QuizDomainData data = new QuizDomainData();
            return data;
        }
        VideoDomainActivityData vad = new VideoDomainActivityData();
        vad.initVideoMetadata("This is just a test" + activityId, "Blah", 1000);
        return vad;
    }
}
