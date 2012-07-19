package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.service.LoadMenuDataService;
import com.ssl.curriculum.math.task.FetchNavigationCategoryTask;

public class NavigationPresenter {
    private NavigationListView navigationListView;

    private Menu menu;

    public NavigationPresenter(NavigationListView navigationListView) {
        this.navigationListView = navigationListView;
    }

    public void loadData() {
        FetchNavigationCategoryTask task = new FetchNavigationCategoryTask(this, new LoadMenuDataService());
        task.execute();
    }

    public void setMenuData(Menu menu) {
        this.menu = menu;
        navigationListView.updateView(menu);
    }
}
