package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.activity.DomainActivityData;

public interface ActivityDataReceiver {
	void onReceivedActivityData(DomainActivityData ad);
}
