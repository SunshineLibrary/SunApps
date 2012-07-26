package com.ssl.curriculum.math.model.menu;

import org.junit.Test;

public class MenuItemTest {

    @Test
    public void test_add_menu_item() {
        Menu menu = Menu.createMenuWithoutParent("First Level Menu", 0);
        MenuItem.createItemAddedToParent("MenuItem 1",1,menu);
        MenuItem.createItemAddedToParent("MenuItem 2",2,menu);
        MenuItem.createItemAddedToParent("MenuItem 3",3,menu);
        assertThat(menu.getChildren().size(), is(3));
    }
}
