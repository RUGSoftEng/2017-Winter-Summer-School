package nl.rug.www.rugsummerschools.controller.generalinfo;

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
import nl.rug.www.rugsummerschools.controller.BasePagerActivity;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

/**
 * This class is an acitivty that allows the general information fragments on this to be slided by.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class GeneralInfoPagerActivity extends BasePagerActivity<GeneralInfo> {

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, GeneralInfoPagerActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, content);
        return intent;
    }

    @Override
    protected List<GeneralInfo> getContents() {
        return ContentsLab.get().getGeneralInfos();
    }

    @Override
    protected Fragment getFragment(int position) {
        GeneralInfo generalInfo = getContents().get(position);
        return GeneralInfoFragment.newInstance(generalInfo.getId());
    }
}
