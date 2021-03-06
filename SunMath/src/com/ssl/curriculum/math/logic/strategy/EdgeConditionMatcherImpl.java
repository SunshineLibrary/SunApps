package com.ssl.curriculum.math.logic.strategy;

import android.util.Log;
import com.ssl.curriculum.math.logic.EdgeConditionMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EdgeConditionMatcherImpl implements EdgeConditionMatcher {

    @Override
    public boolean isMatchedWithCondition(String condition, String result) {
        Pattern pattern = Pattern.compile("CorrectCount\\((\\d+),\\s*(\\d+)\\)");
        Matcher matcher = pattern.matcher(condition);
        if (!matcher.find()) return false;
        if(matcher.groupCount() != 2) return false;
        int minValue, maxValue, resultValue;
        try {
            minValue = Integer.valueOf(matcher.group(1));
            maxValue = Integer.valueOf(matcher.group(2));
        } catch (Exception e) {
            return true;
        }
        try {
            resultValue = Integer.valueOf(result);
        } catch (Exception e) {
            return false;
        }
        return resultValue >= minValue && resultValue <= maxValue;
    }
}
