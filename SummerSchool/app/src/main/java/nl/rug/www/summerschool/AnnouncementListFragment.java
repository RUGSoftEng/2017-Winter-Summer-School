package nl.rug.www.summerschool;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 3/29/17.
 */

public class AnnouncementListFragment extends Fragment {

    private static final String TAG = "AnnounceListFragment";

    private RecyclerView mAnnouncementRecyclerView;
    private List<Announcement> mItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchAnnouncementsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

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
        }
    }
}
