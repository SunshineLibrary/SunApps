package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.adapter.NavigationListAdapter;
import com.ssl.curriculum.math.presenter.NavigationPresenter;

import static com.ssl.curriculum.math.util.Constants.NAVIGATION_ITEM_LABEL;

public class NavigationListView extends ListView {
    private static int[] NAVIGATION_ITEM_ID_LIST = new int[]{R.id.navigation_label};
    private static String[] NAVIGATION_ITEM_LABEL_LIST = new String[]{NAVIGATION_ITEM_LABEL};
    private NavigationListAdapter navigationListAdapter;

    public NavigationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initAdapter(NavigationPresenter presenter) {
        navigationListAdapter = new NavigationListAdapter(getContext(), presenter.getNavigationCategory(), R.layout.navigation_item, NAVIGATION_ITEM_LABEL_LIST, NAVIGATION_ITEM_ID_LIST);
        setAdapter(navigationListAdapter);
    }

    public void updateView() {
        navigationListAdapter.notifyDataSetChanged();
    }
}
