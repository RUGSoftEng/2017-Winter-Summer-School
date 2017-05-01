package nl.rug.www.summerschool.controller.announcement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.networking.NetworkingService;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.Announcement;

/**
 * This class is a fragment on main pager activity.
 * It shows a list of titles of all announcements fetched from database.
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
        new FetchAnnouncementsTask().execute();
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

        setupAdatper();

        return v;
    }

    private void setupAdatper() {
        if (isAdded()) {
            mAnnouncementRecyclerView.setAdapter(new AnnouncementAdapter(mItems));
        }
    }

    private class AnnouncementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Announcement mAnnouncement;
        private TextView mTitleTextView;

        public AnnouncementHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_content, parent, false));

            mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
            itemView.setOnClickListener(this);
        }

        public void bind(Announcement announcement){
            mAnnouncement = announcement;
            mTitleTextView.setText(mAnnouncement.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = AnnouncementPagerActivity.newIntent(getActivity(), mAnnouncement.getId());
            startActivity(intent);
        }
    }

    private class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementHolder> {

        private List<Announcement> mAnnouncements;

        public AnnouncementAdapter(List<Announcement> announcements) {
            mAnnouncements = announcements;
        }

        @Override
        public AnnouncementHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new AnnouncementHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AnnouncementHolder holder, int position) {
            Announcement announcement = mAnnouncements.get(position);
            holder.bind(announcement);
        }

        @Override
        public int getItemCount() {
            return mAnnouncements.size();
        }
    }

    private class FetchAnnouncementsTask extends AsyncTask<Void, Void, List<Announcement>> {

        @Override
        protected List<Announcement> doInBackground(Void... params) {
            return new NetworkingService().fetchAnnouncements();
        }

        @Override
        protected void onPostExecute(List<Announcement> announcements) {
            mItems = announcements;
            setupAdatper();
            ContentsLab.get(getActivity()).updateAnnouncements(mItems);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
