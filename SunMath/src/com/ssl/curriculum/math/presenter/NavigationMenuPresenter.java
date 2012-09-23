package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.activity.NavigationActivity;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;
import com.ssl.curriculum.math.service.NavigationMenuLoaderImpl;
import com.ssl.curriculum.math.service.NavigationMenuLoader;
import com.ssl.curriculum.math.task.FetchNavigationMenuTask;

import java.util.List;

public class NavigationMenuPresenter implements NextLevelMenuChangedListener {
	private NavigationMenuLoader loader;
	private Menu currentMenu;
    private NavigationActivity navigationActivity;

    public NavigationMenuPresenter(NavigationActivity navigationActivity) {
        this.navigationActivity = navigationActivity;
		loader = new NavigationMenuLoaderImpl(navigationActivity);
	}

	public void loadMenuData() {
		FetchNavigationMenuTask task = new FetchNavigationMenuTask(this,
                loader);
		task.execute();
	}

	public void initNavigationMenu(Menu menu) {
		currentMenu = menu;
		updateMenu();
	}

	@Override
	public void onNextLevelMenu(int currentMenuId){
		List<MenuItem> children = currentMenu.getChildren();
		for (int index = 0; index < children.size(); index++) {
			MenuItem item = children.get(index);
			if (item.getId() == currentMenuId) {
				handleMenuItem(item, item.getId());
			}
		}
	}

	private void handleMenuItem(MenuItem item, int id){
		if (!item.isMenuGroup()) {
            navigationActivity.activateMenuItem(id);
            navigationActivity.presentSection(id);
		} else {
            currentMenu = (Menu) item;
            updateMenu();
        }
	}

	public boolean menuBack() {
		if (currentMenu.getParent() == null)
			return false;
        navigationActivity.hideSectionDetails();
		currentMenu = currentMenu.getParent();
		updateMenu();
		return true;
	}

	private void updateMenu() {
        navigationActivity.setMenuTitle(currentMenu.getName());
        navigationActivity.updateMenu(currentMenu);
	}
}
