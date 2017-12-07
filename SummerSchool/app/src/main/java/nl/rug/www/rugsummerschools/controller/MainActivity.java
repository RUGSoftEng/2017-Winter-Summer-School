package nl.rug.www.rugsummerschools.controller;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
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

public class MainActivity extends AppCompatActivity {


    private static final int FRAGMENTS_SIZE = 5;

    private ViewPager mViewPager;
    private BottomNavigationView mNavigation;

    private Fragment[] mFragments = {
            new AnnouncementListFragment(),
            new GeneralInfoListFragment(),
            new LecturerListFragment(),
            new TimeTableFragment(),
            new ForumRootFragment()
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_announcement:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_general:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_lecturer:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_time_table:
                    mViewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_forum:
                    mViewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /************** force to disable shifting mode *************/
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mNavigation.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for(int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        /******************** end of disable ********************/

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 :
                        mNavigation.setSelectedItemId(R.id.navigation_announcement);
                        break;
                    case 1 :
                        mNavigation.setSelectedItemId(R.id.navigation_general);
                        break;
                    case 2 :
                        mNavigation.setSelectedItemId(R.id.navigation_lecturer);
                        break;
                    case 3 :
                        mNavigation.setSelectedItemId(R.id.navigation_time_table);
                        break;
                    case 4 :
                        mNavigation.setSelectedItemId(R.id.navigation_forum);
                        break;
                }
            }
        });
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return mFragments[0];
                    case 1 : return mFragments[1];
                    case 2 : return mFragments[2];
                    case 3 : return mFragments[3];
                    case 4 : return mFragments[4];
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return FRAGMENTS_SIZE;
            }
        });
    }
}
