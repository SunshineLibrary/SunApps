package com.ssl.curriculum.math.model.menu;

public class MenuItem {
    private String name;
    private int db_id = 0;
    public MenuItem(String name, int id) {
        this.name = name;
        this.db_id = id;
    }

    public String getName() {
        return name;
    }
    
    public int getId(){
    	return this.db_id;
    }
    
    public boolean isMenuGroup() {
        return false;
    }

    public static void createItemAddedToParent(String name, int id, Menu parentMenu) {
        parentMenu.addChild(new MenuItem(name,id));
    }
}
