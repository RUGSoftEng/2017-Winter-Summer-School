package nl.rug.www.summerschool.controller.lecturer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.networking.NetworkingService;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.Lecturer;

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
            mLecturerRecyclerView.setAdapter(new LecturerAdapter(mItems));
        }
    }


    private class LecturerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Lecturer mLecturer;
        private TextView mTitleTextView;
        private ImageView mLecturerImageView;

        private LecturerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_lecturer, parent, false));

            mTitleTextView = (TextView)itemView.findViewById(R.id.lecturer_item_name_text_view);
            mLecturerImageView = (ImageView)itemView.findViewById(R.id.lecturer_image_view);
            itemView.setOnClickListener(this);
        }

        private void bind(Lecturer lecturer){
            mLecturer = lecturer;
            mTitleTextView.setText(mLecturer.getTitle());
            Drawable drawable = mLecturer.getProfilePicture();
            if (drawable != null)
                mLecturerImageView.setImageDrawable(drawable);
            else
                mLecturerImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.profile));
        }

        @Override
        public void onClick(View v) {
            Intent intent = LecturerPagerActivity.newIntent(getActivity(), mLecturer.getId());
            startActivity(intent);
        }
    }

    private class LecturerAdapter extends RecyclerView.Adapter<LecturerHolder> {

        private List<Lecturer> mLecturers;

        private LecturerAdapter(List<Lecturer> lecturers) {
            mLecturers = lecturers;
        }

        @Override
        public LecturerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new LecturerHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LecturerHolder holder, int position) {
            Lecturer lecturer = mLecturers.get(position);
            holder.bind(lecturer);
        }

        @Override
        public int getItemCount() {
            return mLecturers.size();
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
