package com.ssl.curriculum.math.listener;

import java.util.ArrayList;
import com.ssl.curriculum.math.model.Edge;

public interface EdgeReceiver {
	void onReceivedEdges(ArrayList<Edge> edges);
}
