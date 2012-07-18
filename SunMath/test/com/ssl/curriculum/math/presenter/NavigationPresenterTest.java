package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.component.NavigationListView;
import com.ssl.curriculum.math.framework.SunMathTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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
        presenter.loadData();
        verify(navigationListView).updateView();
        List<? extends Map<String, ?>> navigationCategory = presenter.getNavigationCategory();
        assertThat(navigationCategory.size(), greaterThan(0));
    }
}
