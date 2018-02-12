package nl.rug.www.rugsummerschools.controller;

import android.content.Context;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import nl.rug.www.rugsummerschools.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static nl.rug.www.rugsummerschools.controller.DrawableMatcher.withDrawable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    /**
     * Passcode must be 8 characters long and contain only lower letters and digits
     */
    private static final String DEFAULT_PASSCODE = "11111111";
    private static final String WRONG_PASSCODE = "11111112";
    private static final String SHORT_PASSCODE = "abcdefg";
    private static final String LONG_PASSCODE = "1o12jij1j21i2ji1ij3";
    private static final String NOT_ALLOWED_PASSCODE = "AB+-aiso";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class, false, false);

    @Before
    public void setUp() {
        File root = getTargetContext().getFilesDir().getParentFile();
        String[] sharedPreferencesFileNames = new File(root, "shared_prefs").list();
        for (String fileName : sharedPreferencesFileNames) {
            getTargetContext().getSharedPreferences(fileName.replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
        }
    }

    @Test
    public void loginTest() {
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.welcome_text)).check(matches(withText(R.string.welcome_to_swapp)));
        onView(withId(R.id.login_button)).check(matches(withText(R.string.log_in_button)));
        onView(withId(R.id.login_rug_logo)).check(matches(withDrawable(R.drawable.ic_rug_white)));
    }

    @Test
    public void emptyPasscodeTest() {
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.code_edit_text)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.passcode_empty_error))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void filterPasscodeTest() {
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.code_edit_text)).perform(clearText()).perform(typeText(SHORT_PASSCODE), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.passcode_restriction))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.code_edit_text)).perform(clearText()).perform(replaceText(LONG_PASSCODE), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.passcode_restriction))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.code_edit_text)).perform(clearText()).perform(replaceText(NOT_ALLOWED_PASSCODE), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.passcode_restriction))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void failPasscodeTest() {
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.code_edit_text)).perform(clearText()).perform(typeText(WRONG_PASSCODE), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.login_failed))
                .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void successPasscodeTest() {
        mActivityTestRule.launchActivity(null);
        onView(withId(R.id.code_edit_text)).perform(clearText()).perform(typeText(DEFAULT_PASSCODE), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
    }
}