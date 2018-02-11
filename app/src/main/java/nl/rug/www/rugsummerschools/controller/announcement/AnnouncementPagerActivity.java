package nl.rug.www.rugsummerschools.controller.announcement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.List;

import nl.rug.www.rugsummerschools.controller.BasePagerActivity;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Announcement;

/**
 * This class is an acitivty that allows the announcement fragments on this to be slided by.
 *
 * @author Jeongkyun Oh
 * @since 13/04/2017
 * @version 2.0.0
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
