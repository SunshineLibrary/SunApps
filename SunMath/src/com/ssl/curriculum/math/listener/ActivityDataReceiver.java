package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.activity.ActivityData;

public interface ActivityDataReceiver {
	void onReceivedActivityData(ActivityData ad);
}
