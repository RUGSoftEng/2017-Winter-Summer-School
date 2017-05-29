package nl.rug.www.rugsummerschool.controller;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.controller.announcement.AnnouncementListFragment;
import nl.rug.www.rugsummerschool.controller.forum.ForumFragment;
import nl.rug.www.rugsummerschool.controller.generalinfo.GeneralInfoListFragment;
import nl.rug.www.rugsummerschool.controller.lecturer.LecturerListFragment;
import nl.rug.www.rugsummerschool.controller.myprofile.RootFragment;
import nl.rug.www.rugsummerschool.controller.timetable.TimeTableFragment;

/**
 * This class is main activity that contains basic layout of the app.
 * It consists of header, viewpager, and buttons.
 * Fragments (Announcements, Generalninfos, ...) will be inflated in view pager.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class MainPagerActivity extends AppCompatActivity {

    private static final int FRAGMENTS_SIZE = 6;

    private ViewPager mViewPager;
    private ImageButton mAnnouncementButton;
    private ImageButton mGeneralInfoButton;
    private ImageButton mLecturerButton;
    private ImageButton mTimeTableButton;
    private ImageButton mForumButton;
    private ImageButton mMyProfileButton;

    private Fragment[] mFragments = {
            new AnnouncementListFragment(),
            new GeneralInfoListFragment(),
            new LecturerListFragment(),
            new TimeTableFragment(),
            new ForumFragment(),
            new RootFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actionBar.setCustomView(R.layout.drawable_actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        mAnnouncementButton = (ImageButton)findViewById(R.id.announcement_button);
        mAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mGeneralInfoButton = (ImageButton) findViewById(R.id.generalinfo_button);
        mGeneralInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        mLecturerButton = (ImageButton) findViewById(R.id.lecturerinfo_button);
        mLecturerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });
        mTimeTableButton = (ImageButton) findViewById(R.id.timetable_button);
        mTimeTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });
        mForumButton = (ImageButton)findViewById(R.id.forum_button);
        mForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(4);
            }
        });
        mMyProfileButton = (ImageButton)findViewById(R.id.my_profile_button);
        mMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(5);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 :
                        return mFragments[0];
                    case 1 :
                        return mFragments[1];
                    case 2 :
                        return mFragments[2];
                    case 3 :
                        return mFragments[3];
                    case 4 :
                        return mFragments[4];
                    case 5 :
                        return mFragments[5];
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return FRAGMENTS_SIZE;
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAnnouncementButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.announcement));
                mGeneralInfoButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.generalinformation));
                mLecturerButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.lecturer));
                mTimeTableButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.timetable));
                mForumButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.forum));
                mMyProfileButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.my_account));
                switch (position) {
                    case 0 :
                        mAnnouncementButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.announcementblack));
                        break;
                    case 1 :
                        mGeneralInfoButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.generalinformationblack));
                        break;
                    case 2 :
                        mLecturerButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.lecturerblack));
                        break;
                    case 3 :
                        mTimeTableButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.timetableblack));
                        break;
                    case 4 :
                        mForumButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.forumblack));
                        break;
                    case 5 :
                        mMyProfileButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.my_accountblack));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
