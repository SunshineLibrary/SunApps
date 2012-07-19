package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.framework.SunMathTestRunner;
import com.ssl.curriculum.math.model.menu.Menu;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SunMathTestRunner.class)
public class NavigationPresenterTest {

    private NavigationListView navigationListView;
    private NavigationPresenter presenter;

    @Before
    public void setUp() throws Exception {
        navigationListView = mock(NavigationListView.class);
        presenter = new NavigationPresenter(navigationListView);
    }

    @Test
    public void test_should_load_data_and_update_view() {
        ArgumentCaptor<Menu> captor = new ArgumentCaptor<Menu>();
        presenter.setMenuData(Menu.createMenuWithoutParent("test"));
        verify(navigationListView).updateView(captor.capture());
        assertThat(captor.getValue().getChildren().size(), is(0));
    }
}
