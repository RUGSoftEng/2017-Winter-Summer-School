package nl.rug.www.rugsummerschools.controller.timetable;

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
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EventDetailActivityTest {

    @Rule
    public ActivityTestRule<EventDetailActivity> mActivityTestRule = new ActivityTestRule<>(EventDetailActivity.class, true, false);


    @Before
    public void setUp() throws Exception {
        List<Event> mEvents = new ArrayList<>();
        Date mStartDate;
        Date mEndDate;
        Event event = new Event();

        event.setId("eventNr1.");
        event.setTitle("new event");
        event.setSchool("summer school");
        event.setLocation("campus");
        event.setDescription("This is test description for event");
        mStartDate = new Date();
        mStartDate.setTime(151827902);
        event.setStartDate(mStartDate);
        mEndDate = new Date();
        mEndDate.setTime(351827902);
        event.setEndDate(mEndDate);
        mEvents.add(event);
        ContentsLab.get().updateEvents(mEvents);

        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = EventDetailActivity.newIntent(targetContext, "eventNr1.");
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void eventdetailTest() {
        onView(withText("new event")).check(matches(anything()));
        onView(withText("campus")).check(matches(anything()));
        onView(withId(R.id.map_view)).check(matches(anything()));
        onView(withText("This is test description for event")).check(matches(anything()));
        Date mStartDate = new Date();
        mStartDate.setTime(151827902);
        Date mEndDate = new Date();
        mEndDate.setTime(351827902);

        SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timePeriod = parseTime.format(mStartDate) + " - " + parseTime.format(mEndDate);
        onView(withText(timePeriod)).check(matches(anything()));
    }
}