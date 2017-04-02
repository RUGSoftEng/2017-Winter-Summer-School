package nl.rug.www.summerschool;

import android.content.Context;
import android.content.Intent;
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

public class LecturerPagerActivity extends AppCompatActivity {

    private static final String EXTRA_LECTURER_ID =
            "nl.rug.www.summerschool.lecturer_id";

    private ViewPager mViewPager;
    private List<Lecturer> mLecturers;

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, LecturerPagerActivity.class);
        intent.putExtra(EXTRA_LECTURER_ID, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pager);

        String lecturerId = (String) getIntent().getSerializableExtra(EXTRA_LECTURER_ID);

        mViewPager = (ViewPager) findViewById(R.id.content_view_pager);

        mLecturers = ContentsLab.get(this).getLecturers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Lecturer lecturer = mLecturers.get(position);
                return LecturerFragment.newInstance(lecturer.getId());
            }

            @Override
            public int getCount() {
                return mLecturers.size();
            }
        });

        for (int i = 0; i < mLecturers.size(); i++) {
            if (mLecturers.get(i).getId().equals(lecturerId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
