package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;
import com.ssl.curriculum.math.service.NavigationMenuProvider;

public class FetchNavigationMenuTask extends AsyncTask<Void, Void, Menu> {
    private NavigationMenuPresenter menuPresenter;
    private NavigationMenuProvider provider;

    public FetchNavigationMenuTask(NavigationMenuPresenter menuPresenter, NavigationMenuProvider provider) {
        this.menuPresenter = menuPresenter;
        this.provider = provider;
    }

    @Override
    protected Menu doInBackground(Void... voids) {
        return provider.loadNavigationMenu();
    }

    @Override
    protected void onPostExecute(Menu menu) {
        menuPresenter.initNavigationMenu(menu);
    }
}