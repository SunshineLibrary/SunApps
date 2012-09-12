package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.activity.NaviActivity;
import com.ssl.curriculum.math.data.SectionHelper;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;
import com.ssl.curriculum.math.model.Section;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;
import com.ssl.curriculum.math.service.NavigationMenuContentProvider;
import com.ssl.curriculum.math.service.NavigationMenuProvider;
import com.ssl.curriculum.math.task.FetchNavigationMenuTask;

import java.util.List;

public class NavigationMenuPresenter implements NextLevelMenuChangedListener {
	private NavigationMenuProvider provider;
	private Menu currentMenu;
    private NaviActivity naviActivity;

    public NavigationMenuPresenter(NaviActivity naviActivity) {
        this.naviActivity = naviActivity;
		provider = new NavigationMenuContentProvider(naviActivity);
	}

	public void loadMenuData() {
		FetchNavigationMenuTask task = new FetchNavigationMenuTask(this,
				provider);
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
            naviActivity.displaySectionDetails();

            naviActivity.activateMenuItem(id);
            naviActivity.setSection(SectionHelper.getSection(naviActivity, id));
            naviActivity.setSectionActivities(SectionHelper.getSectionActivitiesCursor(naviActivity, id));
			return;
		}
		currentMenu = (Menu) item;
		updateMenu();
	}

	public void menuBack() {
		if (currentMenu.getParent() == null)
			return;
        naviActivity.hideSectionDetails();
		currentMenu = currentMenu.getParent();
		updateMenu();
	}

	private void updateMenu() {
        naviActivity.setMenuTitle(currentMenu.getName());
        naviActivity.updateMenu(currentMenu);
	}
}
