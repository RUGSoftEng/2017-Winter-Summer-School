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
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Announcement;

/**
 * This class is an acitivty that allows the announcement fragments on this to be slided by.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class AnnouncementPagerActivity extends AppCompatActivity {

    private static final String EXTRA_ANNOUNCEMENT_ID =
            "nl.rug.www.rugsummerschool.announcement_id";

    private List<Announcement> mAnnouncements;

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, AnnouncementPagerActivity.class);
        intent.putExtra(EXTRA_ANNOUNCEMENT_ID, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pager);

        String announcementId = (String) getIntent().getSerializableExtra(EXTRA_ANNOUNCEMENT_ID);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.content_view_pager);

        mAnnouncements = ContentsLab.get().getAnnouncements();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Announcement announcement = mAnnouncements.get(position);
                return AnnouncementFragment.newInstance(announcement.getId());
            }

            @Override
            public int getCount() {
                return mAnnouncements.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mAnnouncements.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTitle(mAnnouncements.get(mViewPager.getCurrentItem()).getTitle());

        for (int i = 0; i < mAnnouncements.size(); i++) {
            if (mAnnouncements.get(i).getId().equals(announcementId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
