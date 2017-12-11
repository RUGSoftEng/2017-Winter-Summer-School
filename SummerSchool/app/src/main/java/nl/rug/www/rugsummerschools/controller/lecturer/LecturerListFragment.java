package nl.rug.www.rugsummerschools.controller.lecturer;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.controller.ContentsListFragment;
import nl.rug.www.rugsummerschools.model.Lecturer;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is a fragment on main pager activity.
 * It shows a list of titles of all announcements in database.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */
public class LecturerListFragment extends ContentsListFragment<Lecturer, ContentHolder<Lecturer>> {

    @Override
    protected void bindViews() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @StringRes
    @Override
    protected int getSectionStringId() {
        return R.string.lecturer_info;
    }

    @Override
    protected List<Lecturer> fetchContents() {
        return new NetworkingService().fetchLecturers();
    }

    @Override
    protected void update(List<Lecturer> contents) {
        ContentsLab.get().updateLecturers(mItems);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(new ContentAdapter<Lecturer, LecturerHolder>(mItems, getActivity()) {
                @Override
                protected LecturerHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                    return new LecturerHolder(layoutInflater, parent, getActivity()) {
                        @Override
                        public void onClick(View v) {
                            Intent intent = LecturerScrollingActivity.newIntent(getActivity(), mContent.getId());
//                            Intent intent = LecturerActivity.newIntent(getActivity(), mContent.getId());
                            startActivity(intent);
                        }
                    };
                }
            });
        }
    }

}
