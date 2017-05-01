package nl.rug.www.summerschool.controller.generalinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.GeneralInfo;

/**
 * This class is an acitivty that allows the general information fragments on this to be slided by.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class GeneralInfoPagerActivity extends AppCompatActivity {

    private static final String EXTRA_GENERAL_INFO_ID =
            "nl.rug.www.summerschool.generalinfo_id";

    private ViewPager mViewPager;
    private List<GeneralInfo> mGeneralInfos;

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, GeneralInfoPagerActivity.class);
        intent.putExtra(EXTRA_GENERAL_INFO_ID, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pager);

        String generalInfoId = (String) getIntent().getSerializableExtra(EXTRA_GENERAL_INFO_ID);

        mViewPager = (ViewPager) findViewById(R.id.content_view_pager);

        mGeneralInfos = ContentsLab.get(this).getGeneralInfos();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                GeneralInfo generalInfo = mGeneralInfos.get(position);
                return GeneralInfoFragment.newInstance(generalInfo.getId());
            }

            @Override
            public int getCount() {
                return mGeneralInfos.size();
            }
        });

        for (int i = 0; i < mGeneralInfos.size(); i++) {
            if (mGeneralInfos.get(i).getId().equals(generalInfoId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
