package nl.rug.www.summerschool;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by jk on 3/30/17.
 */

public class AnnouncementPagerActivity extends AppCompatActivity {

    private static final String EXTRA_ANNOUNCEMENT_ID =
            "nl.rug.www.summerschool.announcement_id";

    private ViewPager mViewPager;
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

        mViewPager = (ViewPager) findViewById(R.id.content_view_pager);

        mAnnouncements = ContentsLab.get(this).getAnnouncements();
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

        for (int i = 0; i < mAnnouncements.size(); i++) {
            if (mAnnouncements.get(i).getId().equals(announcementId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
