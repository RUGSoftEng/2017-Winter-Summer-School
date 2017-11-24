package nl.rug.www.rugsummerschools.controller;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.announcement.AnnouncementListFragment;
import nl.rug.www.rugsummerschools.controller.forum.ForumRootFragment;
import nl.rug.www.rugsummerschools.controller.generalinfo.GeneralInfoListFragment;
import nl.rug.www.rugsummerschools.controller.lecturer.LecturerListFragment;
import nl.rug.www.rugsummerschools.controller.myprofile.RootFragment;
import nl.rug.www.rugsummerschools.controller.timetable.TimeTableFragment;

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
            new ForumRootFragment(),
            new RootFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            ArrayList<String> mlogInData = new ArrayList<>();
            mlogInData.add(user.getPhotoUrl().toString());
            mlogInData.add(user.getDisplayName());
            mlogInData.add(user.getEmail());
            mlogInData.add(user.getUid());
            ContentsLab.get().setmLogInData(mlogInData);
        }

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
        FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(fragmentManager) {
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

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mAnnouncementButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.announcementblack));
                mGeneralInfoButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.generalinformationblack));
                mLecturerButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.lecturerblack));
                mTimeTableButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.timetableblack));
                mForumButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.forumblack));
                mMyProfileButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.my_accountblack));
                switch (position) {
                    case 0 :
                        mAnnouncementButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.announcement));
                        break;
                    case 1 :
                        mGeneralInfoButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.generalinformation));
                        break;
                    case 2 :
                        mLecturerButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.lecturer));
                        break;
                    case 3 :
                        mTimeTableButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.timetable));
                        break;
                    case 4 :
                        mForumButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.forum));
                        break;
                    case 5 :
                        mMyProfileButton.setImageDrawable(ContextCompat.getDrawable(MainPagerActivity.this, R.drawable.my_account));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
