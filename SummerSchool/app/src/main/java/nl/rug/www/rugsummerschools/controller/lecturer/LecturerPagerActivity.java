package nl.rug.www.rugsummerschools.controller.lecturer;

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
import nl.rug.www.rugsummerschools.controller.generalinfo.GeneralInfoFragment;
import nl.rug.www.rugsummerschools.controller.generalinfo.GeneralInfoPagerActivity;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.model.Lecturer;

/**
 * This class is an acitivty that allows the lecturer fragments on this to be slided by.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class LecturerPagerActivity extends BasePagerActivity<Lecturer> {

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, LecturerPagerActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, content);
        return intent;
    }

    @Override
    protected List<Lecturer> getContents() {
        return ContentsLab.get().getLecturers();
    }

    @Override
    protected Fragment getFragment(int position) {
        Lecturer lecturer = getContents().get(position);
        return LecturerFragment.newInstance(lecturer.getId());
    }
}
