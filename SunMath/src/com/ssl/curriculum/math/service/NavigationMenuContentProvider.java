package com.ssl.curriculum.math.service;

import android.content.Context;
import android.net.Uri;
import com.ssl.curriculum.math.metadata.MetadataContract;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;

public class NavigationMenuContentProvider implements NavigationMenuProvider {
    private Context context;

    public NavigationMenuContentProvider(Context context) {
        this.context = context;
    }

    @Override
    public Menu loadNavigationMenu() {
        Uri uri = MetadataContract.AUTHORITY_URI.buildUpon().appendPath("packages").build();
        String[] columns = {MetadataContract.Courses._NAME};
        context.getContentResolver().query(uri, columns, null, null, null);

        Menu menu = Menu.createMenuWithoutParent("Courses");
        Menu course1 = Menu.createMenuAddedToParent("Course1", menu);
        Menu course2 = Menu.createMenuAddedToParent("Course2", menu);

        Menu chapter1 = Menu.createMenuAddedToParent("chapter1", course1);
        Menu chapter2 = Menu.createMenuAddedToParent("chapter2", course1);
        Menu chapter3 = Menu.createMenuAddedToParent("chapter3", course2);
        Menu chapter4 = Menu.createMenuAddedToParent("chapter4", course2);

        chapter1.addChild(new MenuItem("Lession1"));
        chapter1.addChild(new MenuItem("Lession2"));
        chapter2.addChild(new MenuItem("Lession3"));
        chapter3.addChild(new MenuItem("Lession4"));
        chapter3.addChild(new MenuItem("Lession5"));
        chapter3.addChild(new MenuItem("Lession6"));
        chapter4.addChild(new MenuItem("Lession7"));
        return menu;
    }

}
