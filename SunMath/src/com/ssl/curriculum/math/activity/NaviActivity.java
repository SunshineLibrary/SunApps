package com.ssl.curriculum.math.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ssl.curriculum.math.R;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.presenter.DetailsPagePresenter;
import com.ssl.curriculum.math.presenter.NavigationMenuPresenter;

public class NaviActivity extends Activity {

    private NavigationListView navigationListView;
    private NavigationMenuPresenter menuPresenter;
    private DetailsPagePresenter detailsPresenter;
    private TextView menuTitle;
    private ImageView backImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initComponent();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        menuPresenter.loadMenuData();
    }

    private void initUI() {
        setContentView(R.layout.navigation_layout);
        navigationListView = (NavigationListView) findViewById(R.id.navi_list_view);
        menuTitle = (TextView) findViewById(R.id.navigation_menu_title);
        backImageView =  (ImageView) findViewById(R.id.navigation_menu_back_btn);
    }

    private void initComponent() {
        menuPresenter = new NavigationMenuPresenter(this, navigationListView, menuTitle);
        detailsPresenter = new DetailsPagePresenter();
        
        menuPresenter.setNavigationMenmItemSelectedListener(detailsPresenter);
        
        navigationListView.setNextLevelMenuChangedListener(menuPresenter);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuPresenter.menuBack();
            }
        });

    }
}
