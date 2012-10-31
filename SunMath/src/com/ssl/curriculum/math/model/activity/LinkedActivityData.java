package com.ssl.curriculum.math.model.activity;

import android.util.Log;
import com.ssl.curriculum.math.logic.EdgeConditionMatcher;
import com.ssl.curriculum.math.logic.strategy.EdgeConditionMatcherImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class LinkedActivityData extends DomainActivityData {

    private static final String TAG = "ActivityData";

    public LinkedActivityData defaultNextActivityData;
    public LinkedActivityData defaultPreviousActivityData;
    public LinkedActivityData currentPreviousActivityData;
    private Map<String, LinkedActivityData> conditionalNextActivities;
    private EdgeConditionMatcher edgeConditionMatcher;

    public LinkedActivityData(int type) {
        super(type);
    }

    public void putConditionalNextActivity(String condition, LinkedActivityData activityData) {
        getConditionalNextActivities().put(condition, activityData);
    }

    public LinkedActivityData getNextActivity() {
        Log.d(TAG, "Finding next activity");
        for (Map.Entry<String, LinkedActivityData> entry : getConditionalNextActivities().entrySet()) {
            Log.d("Condition Matcher", entry.getKey() + " " + getResult());
            if (getConditionMatcher().isMatchedWithCondition(entry.getKey(), getResult())) {
                Log.d("Condition Matcher", "Condition Matched");
                return entry.getValue();
            }
            Log.d("Condition Matcher", "Condition Not Matched");
        }
        Log.d(TAG, "No edge found, returning default: " + defaultNextActivityData);
        return defaultNextActivityData;
    }

    public LinkedActivityData getPreviousActivity() {
        if (currentPreviousActivityData == null) {
            return defaultPreviousActivityData;
        }
        return currentPreviousActivityData;
    }

    private Map<String, LinkedActivityData> getConditionalNextActivities() {
        if (conditionalNextActivities == null) {
            conditionalNextActivities = new HashMap<String, LinkedActivityData>();
        }
        return conditionalNextActivities;
    }

    private EdgeConditionMatcher getConditionMatcher() {
        if (edgeConditionMatcher == null) {
            edgeConditionMatcher = new EdgeConditionMatcherImpl();
        }
        return edgeConditionMatcher;
    }
}
