package com.ssl.curriculum.math.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.ssl.curriculum.math.component.NavigationMenuItem;
import com.ssl.curriculum.math.listener.NextLevelMenuChangedListener;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;

public class NavigationMenuListAdapter extends BaseAdapter {
    private Context context;
    private Menu menu;
    private NextLevelMenuChangedListener nextLevelMenuChangedListener;

    public NavigationMenuListAdapter(Context context, Menu menu) {
        this.context = context;
        this.menu = menu;
    }

    public void updateMenu(Menu currentMenu) {
        this.menu = currentMenu;
        notifyDataSetChanged();
    }

    public int getCount() {
        return menu.getChildren().size();
    }

    public Object getItem(int position) {
        if (position >= menu.getChildren().size()) return null;
        return menu.getChildren().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        MenuItem item = (MenuItem)getItem(position);
        return getMenuItem(view, item);
    }

    private NavigationMenuItem getMenuItem(View view, MenuItem menuItem) {
        NavigationMenuItem item;
        if (view == null || !(view instanceof NavigationMenuItem)) {
            item = createNewMenuItem(menuItem.getName(), menuItem.getId());
        } else {
            item = updateMenuItem((NavigationMenuItem) view, menuItem.getName(), menuItem.getId());
            item.deActive();
        }
        return item;
    }

    private NavigationMenuItem updateMenuItem(NavigationMenuItem menuItem, String menuName, int id) {
        menuItem.setMenuName(menuName);
        menuItem.setUniqueId(id);
        return menuItem;
    }

    private NavigationMenuItem createNewMenuItem(final String menuName, final int menuId) {
        NavigationMenuItem menuItem = new NavigationMenuItem(context);
        menuItem.setMenuName(menuName);
        menuItem.setUniqueId(menuId);
        menuItem.setNextLevelChangedListener(nextLevelMenuChangedListener);
        return menuItem;
    }

    public void setNextLevelMenuChangedListener(NextLevelMenuChangedListener nextLevelMenuChangedListener) {
        this.nextLevelMenuChangedListener = nextLevelMenuChangedListener;
    }

}
