package nl.rug.www.rugsummerschools.controller.announcement;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.Content;
import nl.rug.www.rugsummerschools.model.ContentsLab;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AnnouncementPagerActivityTest {

    @Rule
    public ActivityTestRule<AnnouncementPagerActivity> mActivityTestRule = new ActivityTestRule<>(AnnouncementPagerActivity.class, true, false);

    @Before
    public void setUp() {
        List<Announcement> mAnnouncements = new ArrayList<>();
        Announcement[] announcements = new Announcement[] {
                new Announcement(),
                new Announcement(),
                new Announcement()
        };
        for (int i = 0; i < 3; ++i) {
            announcements[i].setId("identificationNo." + i);
            announcements[i].setTitle("title" + i);
            announcements[i].setDescription("Test-driven development (TDD) is a software development process that relies on the repetition of a very short development cycle: Requirements are turned into very specific test cases, then the software is improved to pass the new tests, only. This is opposed to software development that allows software to be added that is not proven to meet requirements." + i);
            announcements[i].setPoster("author" + i);
            announcements[i].setDate("2018-02-08T15:5" + i +":25.346Z");
            mAnnouncements.add(announcements[i]);
        }
        ContentsLab.get().updateAnnouncements(mAnnouncements);

        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = AnnouncementPagerActivity.newIntent(targetContext, "identificationNo.0");
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void announcementDetailCheck() {
        onView(allOf(withText("A"), isDisplayed())).check(matches(anything()));
        onView(allOf(withText("author0"), isDisplayed())).check(matches(anything()));
        onView(allOf(withText("Test-driven development (TDD) is a software development process that relies on the repetition of a very short development cycle: Requirements are turned into very specific test cases, then the software is improved to pass the new tests, only. This is opposed to software development that allows software to be added that is not proven to meet requirements.0"), isDisplayed())).check(matches(anything()));
        Date date = new DateTime("2018-02-08T15:50:25.346Z").toDate();
        SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        onView(allOf(withText(parseDate.format(date)), isDisplayed())).check(matches(anything()));
        onView(allOf(withText(parseTime.format(date)), isDisplayed())).check(matches(anything()));
    }
}