package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.presenter.NavigationPresenter;
import com.ssl.curriculum.math.service.NavigationMenuProvider;

public class FetchNavigationMenuTask extends AsyncTask<Void, Void, Menu> {
    private NavigationPresenter presenter;
    private NavigationMenuProvider provider;

    public FetchNavigationMenuTask(NavigationPresenter presenter, NavigationMenuProvider provider) {
        this.presenter = presenter;
        this.provider = provider;
    }

    @Override
    protected Menu doInBackground(Void... voids) {
        return provider.loadNavigationMenu();
    }

    @Override
    protected void onPostExecute(Menu menu) {
        presenter.updateNavigationMenuData(menu);
    }
}
