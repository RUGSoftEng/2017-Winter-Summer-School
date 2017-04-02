package nl.rug.www.summerschool;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
            new MyProfileFragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
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
        });
    }
}
