package nl.rug.www.rugsummerschools.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.Content;

/**
 * This is base pager activity which is able to slide by each page by page.
 * It applies to announcement only currently.
 *
 * @since 27/11/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public abstract class BasePagerActivity<T extends Content> extends AppCompatActivity {

    protected static final String EXTRA_CONTENT_ID =
            "nl.rug.www.rugsummerschool.content_id";

    protected abstract List<T> getContents();
    protected abstract Fragment getFragment(int position);
    private List<T> mContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pager);

        String contentId = (String) getIntent().getSerializableExtra(EXTRA_CONTENT_ID);

        final ViewPager mViewPager = findViewById(R.id.content_view_pager);

        mContents = getContents();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                return getFragment(position);
            }
            @Override
            public int getCount() {
                return mContents.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTitle(mContents.get(position).getTitle());
            }
        });

        setTitle(mContents.get(mViewPager.getCurrentItem()).getTitle());

        for (int i = 0; i < mContents.size(); i++) {
            if (mContents.get(i).getId().equals(contentId)) {
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
