package nl.rug.www.rugsummerschools.controller.lecturer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Lecturer;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is a fragment on main pager activity.
 * It shows a list of titles of all announcements in database.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */
public class LecturerListFragment extends Fragment {

    private RecyclerView mLecturerRecyclerView;
    private List<Lecturer> mItems = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        TextView section = (TextView)v.findViewById(R.id.section_name);
        section.setText(R.string.lecturer_info);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchLecturersTask().execute();
            }
        });

        mLecturerRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mLecturerRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        setupAdatper();
        new FetchLecturersTask().execute();
        return v;
    }

    private void setupAdatper() {
        if (isAdded()) {
            mLecturerRecyclerView.setAdapter(new ContentAdapter<Lecturer, LecturerHolder>(mItems, getActivity()) {
                @Override
                protected LecturerHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                    return new LecturerHolder(layoutInflater, parent, getActivity()) {
                        @Override
                        public void onClick(View v) {
                            Intent intent = LecturerPagerActivity.newIntent(getActivity(), mContent.getId());
                            startActivity(intent);
                        }
                    };
                }
            });
        }
    }

    private class FetchLecturersTask extends AsyncTask<Void, Void, List<Lecturer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Lecturer> doInBackground(Void... params) {
            return new NetworkingService().fetchLecturers();
        }

        @Override
        protected void onPostExecute(List<Lecturer> lecturers) {
            mItems = lecturers;
            setupAdatper();
            ContentsLab.get().updateLecturers(mItems);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
