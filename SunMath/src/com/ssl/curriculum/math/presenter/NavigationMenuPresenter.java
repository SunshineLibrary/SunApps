package com.ssl.curriculum.math.presenter;

import android.widget.TextView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;
import com.ssl.curriculum.math.service.NavigationMenuContentProvider;
import com.ssl.curriculum.math.service.NavigationMenuProvider;
import com.ssl.curriculum.math.task.FetchNavigationMenuTask;

public class NavigationMenuPresenter implements NextLevelMenuChangedListener {
    private NavigationListView navigationListView;
    private TextView menuTitle;
    private NavigationMenuProvider provider;
    private Menu currentMenu;

    public NavigationMenuPresenter(NavigationListView navigationListView, TextView menuTitle) {
        this.navigationListView = navigationListView;
        this.menuTitle = menuTitle;
        this.navigationListView.setNextLevelMenuChangedListener(this);
        provider = new NavigationMenuContentProvider();
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
        for (MenuItem item : currentMenu.getChildren()) {
            if (item.getName().equalsIgnoreCase(currentMenuItemName)) {
                if (!item.isMenuGroup()) return;
                currentMenu = (Menu) item;
                updateMenu();
            }
        }
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
