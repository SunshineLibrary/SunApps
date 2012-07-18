package com.ssl.curriculum.math.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class NavigationListAdapter extends SimpleAdapter{

    public NavigationListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }
}
