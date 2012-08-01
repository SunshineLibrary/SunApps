package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.activity.NaviActivity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.listener.NavigationMenuItemSelectedListener;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;
import com.ssl.curriculum.math.service.NavigationMenuContentProvider;
import com.ssl.curriculum.math.service.NavigationMenuProvider;
import com.ssl.curriculum.math.task.FetchNavigationMenuTask;

import java.util.List;

public class NavigationMenuPresenter implements NextLevelMenuChangedListener{
	private NavigationListView navigationListView;
	private TextView menuTitle;
	private NavigationMenuProvider provider;
	private Menu currentMenu;
	private NavigationMenuItemSelectedListener menuItemSelectedListener;
	private TextView mztext;
	private LinearLayout test_linear;
	private ListView desListView;
	private Context context;
	

	public NavigationMenuPresenter(Context context,
			NavigationListView navigationListView, TextView menuTitle,
			TextView mztext, LinearLayout test_linear,ListView desListView) {
		this.context=context;
		this.navigationListView = navigationListView;
		this.menuTitle = menuTitle;
		this.mztext = mztext;
		this.test_linear = test_linear;
		this.desListView = desListView;
		
		this.navigationListView.setNextLevelMenuChangedListener(this);
		provider = new NavigationMenuContentProvider(context);
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

	public void setNavigationMenmItemSelectedListener(
			NavigationMenuItemSelectedListener listener) {
		this.menuItemSelectedListener = listener;
	}

	@Override
	public void onNextLevelMenu(int currentMenuId) {
		List<MenuItem> children = currentMenu.getChildren();
		for (int index = 0; index < children.size(); index++) {
			MenuItem item = children.get(index);
			if (item.getId() == currentMenuId) {
				handleMenuItem(item, item.getId());
			}
		}
	}

	private void handleMenuItem(MenuItem item, int index) {
		if (!item.isMenuGroup()) {

			if (this.menuItemSelectedListener != null) {
				this.menuItemSelectedListener.onItemSelected(index);
				
			}

			test_linear.setVisibility(View.VISIBLE);
			navigationListView.activateMenuItem(index);
			NaviActivity.loadRightData(context,desListView, index);
			mztext.setText(item.getName());
			return;
		}
		currentMenu = (Menu) item;
		updateMenu();
	}

	public void menuBack() {
		if (currentMenu.getParent() == null)
			return;
		test_linear.setVisibility(View.INVISIBLE);
		currentMenu = currentMenu.getParent();
		updateMenu();
	}

	private void updateMenu() {
		menuTitle.setText(currentMenu.getName());

		navigationListView.updateMenu(currentMenu);
	}
}
