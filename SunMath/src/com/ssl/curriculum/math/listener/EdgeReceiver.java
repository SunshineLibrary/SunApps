package com.ssl.curriculum.math.listener;

import com.ssl.curriculum.math.model.Edge;

import java.util.ArrayList;

public interface EdgeReceiver {
	void onReceivedEdges(ArrayList<Edge> edges);
}
