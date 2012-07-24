package com.ssl.curriculum.math.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;

import java.util.HashMap;

import static com.ssl.curriculum.math.model.MetadataContract.*;

public class NavigationMenuContentProvider implements NavigationMenuProvider {
    private static final String ROOT_MENU_NAME = "Courses";
    private Context context;
    private final ContentResolver contentResolver;

    public NavigationMenuContentProvider(Context context) {
        this.context = context;
        contentResolver = this.context.getContentResolver();
    }

    @Override
    public Menu loadNavigationMenu() {
        Menu rootMenu = Menu.createMenuWithoutParent(ROOT_MENU_NAME);
        HashMap<Integer, Menu> coursesMap = fetchCoursesMap(rootMenu);
        HashMap<Integer, Menu> chapterMap = fetchChildMenu(coursesMap, Chapters.CONTENT_URI, Chapters._ID, Chapters._PARENT_ID, Chapters._NAME);
        fetchChildMenuItem(chapterMap, Lessons.CONTENT_URI, Lessons._PARENT_ID, Lessons._NAME);
        return rootMenu;
    }

    private void fetchChildMenuItem(HashMap<Integer, Menu> parentMenu, Uri childContentUri, String parentIdColumnName, String nameColumnName) {
        String[] columns = {parentIdColumnName, nameColumnName};
        Cursor cursor = contentResolver.query(childContentUri, columns, null, null, null);
        if (cursor.moveToFirst()) {
            int parentIdIndex = cursor.getColumnIndex(parentIdColumnName);
            int nameIndex = cursor.getColumnIndex(nameColumnName);
            do {
                String name = cursor.getString(nameIndex);
                int parentId = cursor.getInt(parentIdIndex);
                MenuItem.createItemAddedToParent(name, parentMenu.get(parentId));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private HashMap<Integer, Menu> fetchChildMenu(HashMap<Integer, Menu> parentMenu, Uri childContentUri, String idColumnName, String parentIdColumnName, String nameColumnName) {
        HashMap<Integer, Menu> map = new HashMap<Integer, Menu>();
        String[] columns = {idColumnName, parentIdColumnName, nameColumnName};
        Cursor cursor = contentResolver.query(childContentUri, columns, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(idColumnName);
            int parentIdIndex = cursor.getColumnIndex(parentIdColumnName);
            int nameIndex = cursor.getColumnIndex(nameColumnName);
            do {
                String name = cursor.getString(nameIndex);
                int id = cursor.getInt(idIndex);
                int parentId = cursor.getInt(parentIdIndex);
                Menu course = Menu.createMenuAddedToParent(name, parentMenu.get(parentId));
                map.put(id, course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return map;
    }

    private HashMap<Integer, Menu> fetchCoursesMap(Menu rootMenu) {
        HashMap<Integer, Menu> map = new HashMap<Integer, Menu>();
        String[] columns = {Courses._ID, Courses._NAME};
        Cursor cursor = contentResolver.query(Courses.CONTENT_URI, columns, null, null, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(Courses._NAME);
            int idIndex = cursor.getColumnIndex(Courses._ID);
            do {
                String name = cursor.getString(nameIndex);
                int id = cursor.getInt(idIndex);
                Menu course = Menu.createMenuAddedToParent(name, rootMenu);
                map.put(id, course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return map;
    }
}
