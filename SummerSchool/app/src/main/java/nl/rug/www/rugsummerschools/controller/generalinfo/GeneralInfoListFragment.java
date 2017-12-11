package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.controller.ContentsListFragment;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is a fragment on main pager activity.
 * It shows a list of titles of all general infomation in database.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class GeneralInfoListFragment extends ContentsListFragment<GeneralInfo, ContentHolder<GeneralInfo>> {

    @Override
    protected void bindViews() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @StringRes
    @Override
    protected int getSectionStringId() {
        return R.string.general_info;
    }

    @Override
    protected List<GeneralInfo> fetchContents() {
        return new NetworkingService().fetchGeneralInfos();
    }

    @Override
    protected void update(List<GeneralInfo> contents) {
        ContentsLab.get().updateGeneralInfos(mItems);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(new ContentAdapter<GeneralInfo, GeneralInfoHolder>(mItems, getActivity()) {
                @Override
                protected GeneralInfoHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                    return new GeneralInfoHolder(layoutInflater, parent, getActivity()) {

                        @Override
                        public void onClick(View v) {
                            Intent intent = GeneralInfoScrollingActivity.newIntent(getActivity(), mContent.getId());
                            startActivity(intent);
                        }
                    };
                }
            });
        }
    }
}
