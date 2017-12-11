package nl.rug.www.rugsummerschools.controller.announcement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.BasePagerActivity;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.controller.generalinfo.GeneralInfoFragment;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

/**
 * This class is an acitivty that allows the announcement fragments on this to be slided by.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class AnnouncementPagerActivity extends BasePagerActivity<Announcement> {

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, AnnouncementPagerActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, content);
        return intent;
    }

    @Override
    protected List<Announcement> getContents() {
        return ContentsLab.get().getAnnouncements();
    }

    @Override
    protected Fragment getFragment(int position) {
        Announcement announcement = getContents().get(position);
        return AnnouncementFragment.newInstance(announcement.getId());
    }
}
