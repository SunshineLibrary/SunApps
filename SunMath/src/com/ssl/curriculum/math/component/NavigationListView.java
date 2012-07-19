package com.ssl.curriculum.math.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.ssl.curriculum.math.adapter.NavigationMenuListAdapter;
import com.ssl.curriculum.math.model.menu.Menu;

public class NavigationListView extends ListView {
    private NavigationMenuListAdapter navigationMenuListAdapter;

    public NavigationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initAdapter() {
        navigationMenuListAdapter = new NavigationMenuListAdapter(getContext(), Menu.createMenuWithoutParent("Empty"));
        setAdapter(navigationMenuListAdapter);
    }

    public void updateView(Menu currentMenu) {
        navigationMenuListAdapter.updateMenuData(currentMenu);
    }
}
