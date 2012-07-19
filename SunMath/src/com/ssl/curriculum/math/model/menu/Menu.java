package com.ssl.curriculum.math.model.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuItem {
    private List<MenuItem> children = new ArrayList<MenuItem>();

    private Menu parent;

    public static Menu createMenuWithoutParent(String name) {
        return new Menu(name, null);
    }
    
    public static Menu createMenuAddedToParent(String name, Menu parent) {
        return new Menu(name, parent);
    }
    
    private Menu(String name, Menu parent) {
        super(name);
        this.parent = parent;
        if (parent != null) {
            this.parent.addChild(this);
        }
    }

    public Menu getParent() {
        return parent;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void addChild(MenuItem menuItem) {
        children.add(menuItem);
    }
}
