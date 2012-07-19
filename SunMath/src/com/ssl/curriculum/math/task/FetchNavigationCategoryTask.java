package com.ssl.curriculum.math.task;

import android.os.AsyncTask;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.presenter.NavigationPresenter;
import com.ssl.curriculum.math.service.LoadMenuDataService;

public class FetchNavigationCategoryTask extends AsyncTask<Void, Void, Menu> {
    private NavigationPresenter presenter;
    private LoadMenuDataService service;

    public FetchNavigationCategoryTask(NavigationPresenter presenter, LoadMenuDataService service) {
        this.presenter = presenter;
        this.service = service;
    }

    @Override
    protected Menu doInBackground(Void... voids) {
        return service.loadService();
    }

    @Override
    protected void onPostExecute(Menu menu) {
        presenter.setMenuData(menu);
    }
}
