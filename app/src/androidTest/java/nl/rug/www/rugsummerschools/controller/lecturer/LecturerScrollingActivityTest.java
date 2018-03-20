package nl.rug.www.rugsummerschools.controller.lecturer;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Lecturer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class LecturerScrollingActivityTest {

    @Rule
    public ActivityTestRule<LecturerScrollingActivity> mActivityTestRule = new ActivityTestRule<>(LecturerScrollingActivity.class, true, false);

    @Before
    public void setUp() {
        List<Lecturer> mLecturers = new ArrayList<>();
        Lecturer lecturer = new Lecturer();
        lecturer.setId("idNr.1");
        lecturer.setTitle("Alan Turing");
        lecturer.setDescription("Alan Mathison Turing OBE FRS (/ˈtjʊərɪŋ/; 23 June 1912 – 7 June 1954) was an English computer scientist, mathematician, logician, cryptanalyst, philosopher, and theoretical biologist.");
        lecturer.setImgurl("http://www.gogle.com");
        lecturer.setWebsite("https://www.alanturing.com/FiniteAutomaton");
        mLecturers.add(lecturer);
        ContentsLab.get().updateLecturers(mLecturers);

        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = LecturerScrollingActivity.newIntent(targetContext, "idNr.1");
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void lecturerDetailTest() {
        onView(withText("Alan Turing")).check(matches(anything()));
        onView(withText("Alan Mathison Turing OBE FRS (/ˈtjʊərɪŋ/; 23 June 1912 – 7 June 1954) was an English computer scientist, mathematician, logician, cryptanalyst, philosopher, and theoretical biologist.")).check(matches(anything()));
        onView(withId(R.id.fab)).perform(click());
    }
}