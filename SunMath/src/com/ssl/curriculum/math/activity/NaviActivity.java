package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.os.Bundle;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.presenter.NavigationPresenter;

public class NaviActivity extends Activity {

    private NavigationListView navigationListView;
    private NavigationPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initComponent();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.loadMenuData();
    }

    private void initUI() {
        setContentView(R.layout.navigation_layout);
        navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
    }

    private void initComponent() {
        presenter = new NavigationPresenter(navigationListView);
    }
}
