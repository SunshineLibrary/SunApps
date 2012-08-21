package com.ssl.curriculum.math.model.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuItem {
    private List<MenuItem> children = new ArrayList<MenuItem>();
    private Menu parent;

    public static Menu createMenuWithoutParent(String name, int id) {
        return new Menu(name, id,  null);
    }
    
    public static Menu createMenuAddedToParent(String name, int id, Menu parent) {
        return new Menu(name, id, parent);
    }
    
    private Menu(String name, int id, Menu parent) {
        super(name,id);
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

    @Override
    public boolean isMenuGroup() {
        return true;
    }
}
