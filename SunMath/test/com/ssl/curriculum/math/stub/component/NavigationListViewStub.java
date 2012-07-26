package com.ssl.curriculum.math.stub.component;

import android.content.Context;
import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.model.menu.Menu;

public class NavigationListViewStub extends NavigationListView{
    private Menu currentMenu;

    public NavigationListViewStub(Context context) {
        super(context, null);
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    @Override
    public void updateMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

}
