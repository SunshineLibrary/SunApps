package com.ssl.curriculum.math.logic.strategy;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EdgeConditionMatcherImplTest {

    private EdgeConditionMatcherImpl edgeConditionMatcher;

    @Before
    public void setUp() throws Exception {
        edgeConditionMatcher = new EdgeConditionMatcherImpl();
    }

    @Test
    public void test_should_return_true_if_condition_is_not_valid() {
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2)", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2,d)", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(d)", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("e", "2"), is(true));
    }

    @Test
    public void test_should_return_false_if_result_is_not_a_integer() {
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2,3)", "e"), is(false));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2, 3)", "e"), is(false));
    }

    @Test
    public void test_should_return_right() {
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2, 3)", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2, 2)", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2,4)", "2"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(2,6)", "4"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(3,6)", "6"), is(true));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(3,6)", "2"), is(false));
        assertThat(edgeConditionMatcher.isMatchedWithCondition("(3,6)", "7"), is(false));
    }
}
