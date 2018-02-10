package nl.rug.www.rugsummerschools.controller.announcement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.controller.ContentsListFragment;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is a list fragment on main activity.
 * It shows a list of fetched announcements from server filtered with school id.
 *
 * @author Jeongkyun Oh
 * @since 13/04/2017
 * @version 2.0.0
 */

public class AnnouncementListFragment extends ContentsListFragment<Announcement, ContentHolder<Announcement>> {

    @Override
    protected void bindViews() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(
                ContextCompat.getDrawable(getActivity(), R.drawable.divider_horizontal)));
    }

    @StringRes
    @Override
    protected int getSectionStringId() {
        return R.string.announcement;
    }

    @Override
    protected List<Announcement> fetchContents() {
        String schoolId = ContentsLab.get().getSchoolId();
        return new NetworkingService<Announcement>().fetchData(NetworkingService.ANNOUNCEMENT, schoolId);
    }

    @Override
    protected void update(List<Announcement> contents) {
        ContentsLab.get().updateAnnouncements(mItems);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(newAdapter(mItems, getActivity()));
        }
    }

    private ContentAdapter<Announcement, AnnouncementHolder> newAdapter(List<Announcement> items, Context context) {
        return new ContentAdapter<Announcement, AnnouncementHolder>(items, context) {
            @Override
            protected AnnouncementHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                return newHolder(layoutInflater, parent);
            }
        };
    }

    private AnnouncementHolder newHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        return new AnnouncementHolder(layoutInflater, parent) {
            @Override
            public void onClick(View view) {
                Intent intent = AnnouncementPagerActivity.newIntent(getActivity(), mContent.getId());
                startActivity(intent);
            }
        };
    }
}
