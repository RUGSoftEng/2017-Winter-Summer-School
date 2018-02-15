package nl.rug.www.rugsummerschools.controller;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.Html;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.announcement.AnnouncementHolder;
import nl.rug.www.rugsummerschools.controller.generalinfo.GeneralInfoHolder;
import nl.rug.www.rugsummerschools.controller.lecturer.LecturerHolder;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.model.Lecturer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String DEFAULT_SCHOOL_ID = "5a5d15a9eff28d521340b136";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null) {
            auth.signOut();
        }
        LoginManager.getInstance().logOut();
        ContentsLab.get().getSchoolInfo().setSchoolId(DEFAULT_SCHOOL_ID);
    }

    @Test
    public void allFragmentsDisplayed() {
        onView(allOf(withId(R.id.section_name), withText(R.string.announcement))).check(matches(isDisplayed()));
        onView(withId(R.id.main_view_pager)).perform(swipeLeft());
        onView(allOf(withId(R.id.section_name), withText(R.string.general_info
        ))).check(matches(isDisplayed()));
        onView(withId(R.id.main_view_pager)).perform(swipeLeft());
        onView(allOf(withId(R.id.section_name), withText(R.string.lecturer_info
        ))).check(matches(isDisplayed()));
        onView(withId(R.id.main_view_pager)).perform(swipeLeft());
        onView(allOf(withId(R.id.section_name), withText(R.string.time_table
        ))).check(matches(isDisplayed()));
        onView(withId(R.id.main_view_pager)).perform(swipeLeft());
    }

    @Test
    public void bottomSheetTest() {
        onView(withId(R.id.navigation)).check(matches(hasDescendant(withText(R.string.notice))));
        onView(withId(R.id.navigation)).check(matches(hasDescendant(withText(R.string.info))));
        onView(withId(R.id.navigation)).check(matches(hasDescendant(withText(R.string.staffs))));
        onView(withId(R.id.navigation)).check(matches(hasDescendant(withText(R.string.schedule))));
        onView(withId(R.id.navigation)).check(matches(hasDescendant(withText(R.string.forum))));
    }

    @Test
    public void announcementTest() {
        onView(withId(R.id.navigation_announcement)).perform(click());
        List<Announcement> list = ContentsLab.get().getAnnouncements();
        if (list == null) return;

        int i = 0;
        for (Announcement announcement : list) {
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.scrollToPosition(i));
            onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                    .check(matches(hasDescendant(withText(announcement.getInitial()))));
            onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                    .check(matches(hasDescendant(withText("By " + announcement.getPoster()))));
            onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                    .check(matches(hasDescendant(withText(announcement.getTitle()))));
            onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                    .check(matches(hasDescendant(withText(announcement.getInitial()))));
            i++;
        }
        assertThat(list.size(), is(i));
        if (i != 0)
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.<AnnouncementHolder>actionOnItemAtPosition(0, click()));
    }

    @Test
    public void generalInfoTest() {
        onView(withId(R.id.navigation_general)).perform(click());
        List<GeneralInfo> list = ContentsLab.get().getGeneralInfos();
        if (list == null) return;

        int i = 0;
        for (GeneralInfo generalInfo : list) {
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.scrollToPosition(i));
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).check(matches(hasDescendant(withText(generalInfo.getTitle()))));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                onView(allOf(withId(R.id.recycler_view), isDisplayed())).check(matches(hasDescendant(withText(Html.fromHtml(generalInfo.getDescription(), Html.FROM_HTML_MODE_LEGACY).toString()))));
            } else {
                onView(allOf(withId(R.id.recycler_view), isDisplayed())).check(matches(hasDescendant(withText(Html.fromHtml(generalInfo.getDescription()).toString()))));
            }
            i++;
        }
        assertThat(list.size(), is(i));
        if (i != 0)
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.<GeneralInfoHolder>actionOnItemAtPosition(0, click()));
    }

    @Test
    public void lecturerTest() {
        onView(withId(R.id.navigation_lecturer)).perform(click());
        List<Lecturer> list = ContentsLab.get().getLecturers();
        if (list == null) return;

        int i = 0;
        for (Lecturer lecturer : list) {
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.scrollToPosition(i));
            onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                    .check(matches(hasDescendant(withText(lecturer.getTitle()))));
            i++;
        }
        assertThat(list.size(), is(i));
        if (i != 0)
            onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.<LecturerHolder>actionOnItemAtPosition(0, click()));
    }

    @Test
    public void timeTableTest() {
        onView(withId(R.id.navigation_time_table)).perform(click());
        List<Event> list = ContentsLab.get().getEvents();
        if (list == null) return;
    }

    @Test
    public void forumTest() {
        onView(withId(R.id.navigation_forum)).perform(click());
        List<GeneralInfo> list = ContentsLab.get().getGeneralInfos();
        if (list == null) return;
    }
}