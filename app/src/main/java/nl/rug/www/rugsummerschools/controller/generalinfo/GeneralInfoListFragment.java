package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.controller.ContentsListFragment;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is a fragment on main activity.
 * It shows a list of titles of all general information fetched from server.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 13/04/2017
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
        return new NetworkingService<GeneralInfo>().fetchData(NetworkingService.GENERAL_INFO, null);
    }

    @Override
    protected void update(List<GeneralInfo> contents) {
        ContentsLab.get().updateGeneralInfos(mItems);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(newAdapter(mItems));
        }
    }

    private ContentAdapter<GeneralInfo, GeneralInfoHolder> newAdapter(List<GeneralInfo> item) {
        return new ContentAdapter<GeneralInfo, GeneralInfoHolder>(item, getActivity()) {
            @Override
            protected GeneralInfoHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                return newHolder(layoutInflater, parent);
            }
        };
    }

    private GeneralInfoHolder newHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        return new GeneralInfoHolder(layoutInflater, parent, getActivity()) {
            @Override
            public void onClick(View view) {
                Intent intent = GeneralInfoScrollingActivity.newIntent(getActivity(), mContent.getId());
                startActivity(intent);
            }
        };
    }
}
