package nl.rug.www.rugsummerschools;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class, true, false);

    @Test
    public void splashTest() {
        mActivityTestRule.launchActivity(null);
    }

}