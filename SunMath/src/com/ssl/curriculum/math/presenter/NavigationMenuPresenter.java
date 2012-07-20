package com.ssl.curriculum.math.presenter;

import android.content.Context;
import android.widget.TextView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;
import com.ssl.curriculum.math.service.NavigationMenuContentProvider;
import com.ssl.curriculum.math.service.NavigationMenuProvider;
import com.ssl.curriculum.math.task.FetchNavigationMenuTask;

import java.util.List;

public class NavigationMenuPresenter implements NextLevelMenuChangedListener {
    private NavigationListView navigationListView;
    private TextView menuTitle;
    private NavigationMenuProvider provider;
    private Menu currentMenu;

    public NavigationMenuPresenter(Context context, NavigationListView navigationListView, TextView menuTitle) {
        this.navigationListView = navigationListView;
        this.menuTitle = menuTitle;
        this.navigationListView.setNextLevelMenuChangedListener(this);
        provider = new NavigationMenuContentProvider(context);
    }

    public void loadMenuData() {
        FetchNavigationMenuTask task = new FetchNavigationMenuTask(this, provider);
        task.execute();
    }

    public void initNavigationMenu(Menu menu) {
        currentMenu = menu;
        updateMenu();
    }

    @Override
    public void onNextLevelMenu(String currentMenuItemName) {
        List<MenuItem> children = currentMenu.getChildren();
        for (int index = 0; index < children.size(); index++) {
            MenuItem item = children.get(index);
            if (item.getName().equalsIgnoreCase(currentMenuItemName)) {
                handleMenuItem(item, index);
            }
        }
    }

    private void handleMenuItem(MenuItem item, int index) {
        if (!item.isMenuGroup()) {
            navigationListView.deActiveAllMenuItems();
            navigationListView.activeMenuItem(index);
            return;
        }
        currentMenu = (Menu) item;
        updateMenu();
    }

    public void menuBack() {
        if (currentMenu.getParent() == null) return;
        currentMenu = currentMenu.getParent();
        updateMenu();
    }

    private void updateMenu() {
        menuTitle.setText(currentMenu.getName());
        navigationListView.updateMenu(currentMenu);
    }
}
