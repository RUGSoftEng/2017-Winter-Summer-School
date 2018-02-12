package nl.rug.www.rugsummerschools.controller.generalinfo;

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

import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GeneralInfoScrollingActivityTest {

    @Rule
    public ActivityTestRule<GeneralInfoScrollingActivity> mActivityTestRule = new ActivityTestRule<>(GeneralInfoScrollingActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        List<GeneralInfo> mGeneralInfos = new ArrayList<>();
        GeneralInfo generalInfo = new GeneralInfo();
        generalInfo.setId("5a7c73a1ab30fc76a07497d3.");
        generalInfo.setTitle("title00");
        generalInfo.setDescription("The static architecture-conformance techniques that we compare are dependency-structure matrices (DSMs),4 source code query languages (SCQLs),5 and reflexion models (RMs).6 We chose these particular techniques because they're representative of the spectrum of available solutions for static architecture conformance, and they're supported by mature and industrial-strength tools that you can apply to systems written in Java. We describe other existing techniques for architecture-conformance analysis in the “Related Work on Architecture Conformance” sidebar.");
        generalInfo.setCategory("Info");
        mGeneralInfos.add(generalInfo);
        ContentsLab.get().updateGeneralInfos(mGeneralInfos);

        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = GeneralInfoScrollingActivity.newIntent(targetContext, "5a7c73a1ab30fc76a07497d3.");
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void generalinfoDetailTest() {
        onView(withText("title00")).check(matches(anything()));
        onView(withText("The static architecture-conformance techniques that we compare are dependency-structure matrices (DSMs),4 source code query languages (SCQLs),5 and reflexion models (RMs).6 We chose these particular techniques because they're representative of the spectrum of available solutions for static architecture conformance, and they're supported by mature and industrial-strength tools that you can apply to systems written in Java. We describe other existing techniques for architecture-conformance analysis in the “Related Work on Architecture Conformance” sidebar.")).check(matches(anything()));
    }
}