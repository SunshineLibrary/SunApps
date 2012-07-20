package com.ssl.curriculum.math.model.menu;

public class MenuItem {
    private String name;

    public MenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isMenuGroup() {
        return false;
    }

    public static void createItemAddedToParent(String name, Menu parentMenu) {
        parentMenu.addChild(new MenuItem(name));
    }
}
