package nl.rug.www.rugsummerschool.controller.announcement;

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

import nl.rug.www.rugsummerschool.controller.ContentsLab;
import nl.rug.www.rugsummerschool.networking.NetworkingService;
import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.model.Announcement;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

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
                ContextCompat.getDrawable(getActivity(), R.drawable.horizontaldivider)));

        setupAdatper();

        new FetchAnnouncementsTask().execute();
        return v;
    }

    private void setupAdatper() {
        if (isAdded()) {
            mAnnouncementRecyclerView.setAdapter(new AnnouncementAdapter(mItems));
        }
    }

    private class AnnouncementHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Announcement mAnnouncement;
        private TextView mInitialView;
        private TextView mTitleTextView;
        private TextView mAuthorTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private TextView mNewTextView;

        private AnnouncementHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_announcement, parent, false));

            mInitialView = (TextView)itemView.findViewById(R.id.initial_text_view);
            mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
            mAuthorTextView = (TextView)itemView.findViewById(R.id.author_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.date);
            mTimeTextView = (TextView)itemView.findViewById(R.id.time);
            mNewTextView = (TextView)itemView.findViewById(R.id.new_image_view);
            itemView.setOnClickListener(this);
        }

        private int generateColor(String name) {
            int hash = name.hashCode();
            int r = (hash & 0xFF0000) >> 16;
            int g = (hash & 0x00FF00) >> 8;
            int b = hash & 0x0000FF;
            return Color.rgb(r, g, b);
        }

        private void bind(Announcement announcement) {
            mAnnouncement = announcement;
            mTitleTextView.setText(mAnnouncement.getTitle());
            String poster = mAnnouncement.getPoster();
            mInitialView.setText(poster.toUpperCase().charAt(0) + "");
            GradientDrawable circle = (GradientDrawable)mInitialView.getBackground();
            circle.setColor(generateColor(poster));
            mAuthorTextView.setText("By " + poster);
            Date date = new DateTime(mAnnouncement.getDate()).toDate();
            Date today = new Date();
            if (today.getTime() - date.getTime() < MILLIS_PER_DAY) {
                mNewTextView.setVisibility(View.VISIBLE);
            } else {
                mNewTextView.setVisibility(View.GONE);
            }
            SimpleDateFormat parseDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            mDateTextView.setText(parseDate.format(date));
            mTimeTextView.setText(parseTime.format(date));
        }

        @Override
        public void onClick(View v) {
            Intent intent = AnnouncementPagerActivity.newIntent(getActivity(), mAnnouncement.getId());
            startActivity(intent);
        }
    }

    private class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementHolder> {

        private List<Announcement> mAnnouncements;

        private AnnouncementAdapter(List<Announcement> announcements) {
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
