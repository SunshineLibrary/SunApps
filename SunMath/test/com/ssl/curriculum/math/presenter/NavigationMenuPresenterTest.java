package com.ssl.curriculum.math.presenter;

import android.app.Activity;
import android.widget.TextView;
import com.ssl.curriculum.math.framework.SunMathTestRunner;
import com.ssl.curriculum.math.model.menu.Menu;
import com.ssl.curriculum.math.model.menu.MenuItem;
import com.ssl.curriculum.math.stub.component.NavigationListViewStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SunMathTestRunner.class)
public class NavigationMenuPresenterTest {

    private NavigationListViewStub navigationListView;
    private NavigationMenuPresenter menuPresenter;
    private Menu menu;
    private TextView menuTitle;

    @Before
    public void setUp() throws Exception {
        navigationListView = new NavigationListViewStub(null);
        menuTitle = new TextView(new Activity());
        menuPresenter = new NavigationMenuPresenter(navigationListView, menuTitle);
        menu = mockData();
        menuPresenter.initNavigationMenu(menu);
    }

    @Test
    public void test_label_update() {
        menuPresenter.onNextLevelMenu("Course1");
        assertThat(menuTitle.getText().toString(), is("Course1"));
        menuPresenter.onNextLevelMenu("chapterNonexistant");
        assertThat(menuTitle.getText().toString(), is("Course1"));
    }

    @Test
    public void test_walk_menu() {
        menuPresenter.onNextLevelMenu("NonExistant");
        assertThat(menuTitle.getText().toString(), is("Courses"));
        assertThat(navigationListView.getCurrentMenu().getName(), is("Courses"));
        assertThat(navigationListView.getCurrentMenu().getChildren().size(), is(2));

        menuPresenter.onNextLevelMenu("Course1");
        assertThat(navigationListView.getCurrentMenu().getName(), is("Course1"));
        assertThat(navigationListView.getCurrentMenu().getChildren().size(), is(2));
        assertThat(menuTitle.getText().toString(), is("Course1"));

        menuPresenter.onNextLevelMenu("chapter1");
        assertThat(navigationListView.getCurrentMenu().getName(), is("chapter1"));
        assertThat(navigationListView.getCurrentMenu().getChildren().size(), is(3));
        assertThat(menuTitle.getText().toString(), is("chapter1"));

        menuPresenter.onNextLevelMenu("Lession1");
        assertThat(navigationListView.getCurrentMenu().getName(), is("chapter1"));
        assertThat(navigationListView.getCurrentMenu().getChildren().size(), is(3));
        assertThat(menuTitle.getText().toString(), is("chapter1"));

        menuPresenter.menuBack();
        assertThat(navigationListView.getCurrentMenu().getName(), is("Course1"));
        assertThat(navigationListView.getCurrentMenu().getChildren().size(), is(2));
        assertThat(menuTitle.getText().toString(), is("Course1"));

        menuPresenter.menuBack();
        assertThat(navigationListView.getCurrentMenu().getName(), is("Courses"));
        assertThat(navigationListView.getCurrentMenu().getChildren().size(), is(2));
        assertThat(menuTitle.getText().toString(), is("Courses"));
    }

    private Menu mockData() {
        Menu menu = Menu.createMenuWithoutParent("Courses");
        Menu course1 = Menu.createMenuAddedToParent("Course1", menu);
        Menu course2 = Menu.createMenuAddedToParent("Course2", menu);

        Menu chapter1 = Menu.createMenuAddedToParent("chapter1", course1);
        Menu chapter2 = Menu.createMenuAddedToParent("chapter2", course1);
        Menu chapter3 = Menu.createMenuAddedToParent("chapter3", course2);
        Menu chapter4 = Menu.createMenuAddedToParent("chapter4", course2);
        chapter1.addChild(new MenuItem("Lession1"));
        chapter1.addChild(new MenuItem("Lession2"));
        chapter1.addChild(new MenuItem("Lession3"));
        chapter2.addChild(new MenuItem("Lession4"));
        chapter3.addChild(new MenuItem("Lession4"));
        chapter3.addChild(new MenuItem("Lession5"));
        chapter3.addChild(new MenuItem("Lession6"));
        chapter4.addChild(new MenuItem("Lession7"));
        return menu;
    }
}
