package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

    private HashMap<String, Integer> mPicHashMap;
    private String[] mStrings = {"weather", "visa", "house", "departure", "insurance", "financial", "do", "welcome", "diet", "internet", "information", "location"};

    @Override
    protected void bindViews() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicHashMap = new HashMap<>();
        mPicHashMap.put(mStrings[0], R.mipmap.ic_gen_info_cloud);
        mPicHashMap.put(mStrings[1], R.mipmap.ic_gen_info_creditcard);
        mPicHashMap.put(mStrings[2], R.mipmap.ic_gen_info_building);
        mPicHashMap.put(mStrings[3], R.mipmap.ic_gen_info_flight);
        mPicHashMap.put(mStrings[4], R.mipmap.ic_gen_info_hospital);
        mPicHashMap.put(mStrings[5], R.mipmap.ic_gen_info_money);
        mPicHashMap.put(mStrings[6], R.mipmap.ic_gen_info_list);
        mPicHashMap.put(mStrings[7], R.mipmap.ic_gen_info_home);
        mPicHashMap.put(mStrings[8], R.mipmap.ic_gen_info_diet);
        mPicHashMap.put(mStrings[9], R.mipmap.ic_gen_info_internet);
        mPicHashMap.put(mStrings[10], R.mipmap.ic_gen_info_info);
        mPicHashMap.put(mStrings[11], R.mipmap.ic_gen_info_loc);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(new ContentAdapter<GeneralInfo, GeneralInfoHolder>(mItems, getActivity()) {
                @Override
                protected GeneralInfoHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                    return new GeneralInfoHolder(layoutInflater, parent) {
                        @Override
                        protected String[] getStrings() {
                            return mStrings;
                        }

                        @Override
                        protected HashMap<String, Integer> getPicHashMap() {
                            return mPicHashMap;
                        }

                        @Override
                        public void onClick(View v) {
                            Intent intent = GeneralInfoPagerActivity.newIntent(getActivity(), mContent.getId());
                            startActivity(intent);
                        }
                    };
                }
            });
        }
    }
}
