package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;
import com.ssl.curriculum.math.service.NavigationMenuLoader;

public class FetchNavigationMenuTask extends AsyncTask<Void, Void, Menu> {
	private NavigationMenuPresenter menuPresenter;
	private NavigationMenuLoader loader;

	public FetchNavigationMenuTask(NavigationMenuPresenter menuPresenter,
			NavigationMenuLoader loader) {
		this.menuPresenter = menuPresenter;
		this.loader = loader;
	}

	@Override
	protected Menu doInBackground(Void... voids) {
		return loader.loadNavigationMenu();
	}

	@Override
	protected void onPostExecute(Menu menu) {
		menuPresenter.initNavigationMenu(menu);
	}

}
