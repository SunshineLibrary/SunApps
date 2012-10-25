package com.ssl.curriculum.math.model.activity;

import android.database.Cursor;
import android.util.Log;
import com.ssl.curriculum.math.logic.EdgeConditionMatcher;
import com.ssl.curriculum.math.logic.strategy.EdgeConditionMatcherImpl;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class LinkedActivityData extends DomainActivityData {

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
        for (Map.Entry<String, LinkedActivityData> entry : getConditionalNextActivities().entrySet()) {
            Log.d("Condition Matcher", entry.getKey() + " " + getResult());
            if (getConditionMatcher().isMatchedWithCondition(entry.getKey(), getResult())) {
                Log.d("Condition Matcher", "Condition Matched");
                return entry.getValue();
            }
            Log.d("Condition Matcher", "Condition Not Matched");
        }
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
