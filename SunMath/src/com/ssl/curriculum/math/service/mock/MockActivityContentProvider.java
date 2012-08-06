package com.ssl.curriculum.math.service.mock;


import android.content.Context;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.model.activity.QuizDomainData;
import com.ssl.curriculum.math.model.activity.VideoDomainActivityData;
import com.ssl.curriculum.math.model.activity.quiz.QuizFillBlankQuestion;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.sunshine.metadata.provider.MetadataContract.Activities;

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
//            activityData.addQuestion(new QuizMultichoiceQuestion());
//            activityData.addQuestion(new QuizMultichoiceQuestion());
            data.addQuestion(new QuizFillBlankQuestion());
            data.addQuestion(new QuizFillBlankQuestion());
            return data;
        }
        return new DomainActivityData(Activities.TYPE_GALLERY);
    }
}
