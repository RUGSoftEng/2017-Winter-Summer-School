package nl.rug.www.rugsummerschools.controller.forum;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.rug.www.rugsummerschools.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class ThreadActivityTest {

    @Rule
    public ActivityTestRule<ThreadActivity> mActivityTestRule = new ActivityTestRule<>(ThreadActivity.class);

    @Test
    public void threadPostTest() {
        onView(withId(R.id.title_edit_text)).check(matches(anything()));
        onView(withId(R.id.contents_edit_text)).check(matches(anything()));
        onView(withId(R.id.post_menu)).check(matches(anything()));
    }
}