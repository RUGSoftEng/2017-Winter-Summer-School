package nl.rug.www.rugsummerschools.controller.announcement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

/**
 * This class is a fragment on main pager activity.
 * It shows a list of titles of all announcements fetched from server.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class AnnouncementListFragment extends Fragment {

    /** recycler view inflates list of announcement by using viewholder */
    private RecyclerView mAnnouncementRecyclerView;

    /** instance of the announcement list */
    private List<Announcement> mItems = new ArrayList<>();

    /** refresh layout in order to update lists of announcement */
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
        section.setText(R.string.announcement);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchAnnouncementsTask().execute();
            }
        });
        if (mItems == null)
            mSwipeRefreshLayout.setRefreshing(true);

        mAnnouncementRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mAnnouncementRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAnnouncementRecyclerView.addItemDecoration(new DividerItemDecoration(
                ContextCompat.getDrawable(getActivity(), R.drawable.divider_horizontal)));

        setupAdatper();

        new FetchAnnouncementsTask().execute();
        return v;
    }

    private void setupAdatper() {
        if (isAdded()) {
            mAnnouncementRecyclerView.setAdapter(new ContentAdapter<Announcement, AnnouncementHolder>(mItems, getActivity()) {
                @Override
                protected AnnouncementHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                    return new AnnouncementHolder(layoutInflater, parent) {
                        @Override
                        public void onClick(View v) {
                            Intent intent = AnnouncementPagerActivity.newIntent(getActivity(), mContent.getId());
                            startActivity(intent);
                        }
                    };
                }
            });
        }
    }

    private class FetchAnnouncementsTask extends AsyncTask<Void, Void, List<Announcement>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Announcement> doInBackground(Void... params) {
            return new NetworkingService().fetchAnnouncements();
        }

        @Override
        protected void onPostExecute(List<Announcement> announcements) {
            mItems = announcements;
            setupAdatper();
            ContentsLab.get().updateAnnouncements(mItems);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
