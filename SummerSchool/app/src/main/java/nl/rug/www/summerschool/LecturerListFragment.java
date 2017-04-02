package nl.rug.www.summerschool;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 3/29/17.
 */

public class LecturerListFragment extends Fragment {

    private static final String TAG = "LecturerListFragment";

    private RecyclerView mLecturerRecyclerView;
    private List<Lecturer> mItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mItems = ContentsLab.get(getActivity()).getLecturers();
        //TODO : fetch lecturer data from database
//        new FetchLecturersTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mLecturerRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mLecturerRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        setupAdatper();

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

        public LecturerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_lecturer, parent, false));

            mTitleTextView = (TextView)itemView.findViewById(R.id.lecturer_item_name_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(Lecturer lecturer){
            mLecturer = lecturer;
            mTitleTextView.setText(mLecturer.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = LecturerPagerActivity.newIntent(getActivity(), mLecturer.getId());
            startActivity(intent);
        }
    }

    private class LecturerAdapter extends RecyclerView.Adapter<LecturerHolder> {

        private List<Lecturer> mLecturers;

        public LecturerAdapter(List<Lecturer> lecturers) {
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

//    private class FetchLecturersTask extends AsyncTask<Void, Void, List<Announcement>> {
//
//        @Override
//        protected List<Lecturer> doInBackground(Void... params) {
//            return new NetworkingService().fetchLecturers();
//        }
//
//        @Override
//        protected void onPostExecute(List<Lecturer> lecturers) {
//            mItems = lecturers;
//            setupAdatper();
//            ContentsLab.get(getActivity()).updateAnnouncements(mItems);
//        }
//    }
}
