package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationPresenter {
    private NavigationListView navigationListView;

    private List<HashMap<String, String>> navigationCategory = new ArrayList<HashMap<String, String>>();

    public NavigationPresenter(NavigationListView navigationListView) {
        this.navigationListView = navigationListView;
    }

    public void loadData() {
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(Constants.NAVIGATION_ITEM_LABEL, "Lession_" + i);
            navigationCategory.add(hashMap);
        }
        navigationListView.updateView();
    }

    public List<? extends Map<String, ?>> getNavigationCategory() {
        return navigationCategory;
    }

}
