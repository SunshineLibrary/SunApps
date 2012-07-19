package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.service.NavigationMenuContentProvider;
import com.ssl.curriculum.math.service.NavigationMenuProvider;
import com.ssl.curriculum.math.task.FetchNavigationMenuTask;

public class NavigationPresenter {
    private NavigationListView navigationListView;
    private NavigationMenuProvider provider;
    private Menu menu;

    public NavigationPresenter(NavigationListView navigationListView) {
        this.navigationListView = navigationListView;
        provider = new NavigationMenuContentProvider();
    }

    public void loadMenuData() {
        FetchNavigationMenuTask task = new FetchNavigationMenuTask(this, provider);
        task.execute();
    }

    public void updateNavigationMenuData(Menu menu) {
        this.menu = menu;
        navigationListView.updateMenuList(menu);
    }
}
