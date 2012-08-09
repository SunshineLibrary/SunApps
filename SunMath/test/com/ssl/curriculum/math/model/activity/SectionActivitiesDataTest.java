package com.ssl.curriculum.math.model.activity;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class SectionActivitiesDataTest {

    private SectionActivitiesData sectionActivitiesData;

    @Before
    public void setUp() throws Exception {
        sectionActivitiesData = new SectionActivitiesData();
        sectionActivitiesData.addSectionActivity(new SectionActivityData(1, 1));
        sectionActivitiesData.addSectionActivity(new SectionActivityData(2, 2));
        sectionActivitiesData.addSectionActivity(new SectionActivityData(3, 4));
        sectionActivitiesData.addSectionActivity(new SectionActivityData(4, 7));
        sectionActivitiesData.addSectionActivity(new SectionActivityData(5, 10));
    }

    @Test
    public void test_should_get_right_data() {
        SectionActivityData sectionActivityData = sectionActivitiesData.getSectionActivityBySequence(2);
        assertThat(sectionActivityData.activityId, is(3));
        sectionActivityData = sectionActivitiesData.getSectionActivityBySequence(4);
        assertThat(sectionActivityData.activityId, is(4));

        sectionActivityData = sectionActivitiesData.getSectionActivityBySequence(1);
        assertThat(sectionActivityData.activityId, is(2));

        sectionActivityData = sectionActivitiesData.getSectionActivityBySequence(7);
        assertThat(sectionActivityData.activityId, is(5));

        sectionActivityData = sectionActivitiesData.getSectionActivityBySequence(10);
        assertNull(sectionActivityData);
    }
}
