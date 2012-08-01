package com.ssl.curriculum.math.model.menu;

import com.ssl.curriculum.math.utils.QuizTemplateResolver;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class QuizHtmlTemplateTest {

    @Test
    public void test_should_replace_body_element() {
        String quizRawHtmlString = "<html><body>eee</body></html>";
        String htmlString = QuizTemplateResolver.replaceWithNewContent(quizRawHtmlString, "fff");
        assertThat(htmlString, is("<html><body>fff</body></html>"));
    }

    @Test
    public void test_should_replace_nothing_given_no_body_element() {
        String quizRawHtmlString = "<html>eee</html>";
        String htmlString = QuizTemplateResolver.replaceWithNewContent(quizRawHtmlString, "fff");
        assertThat(htmlString, is("<html>eee</html>"));
    }

    @Test
    public void test_should_replace_when_new_content_with_some_tags() {
        String quizRawHtmlString = "<html><body><p>pp</p></body></html>";
        String htmlString = QuizTemplateResolver.replaceWithNewContent(quizRawHtmlString, "<span>fff</span>");
        assertThat(htmlString, is("<html><body><span>fff</span></body></html>"));
    }

}
