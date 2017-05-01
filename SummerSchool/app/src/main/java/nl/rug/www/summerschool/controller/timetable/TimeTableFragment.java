package nl.rug.www.summerschool.controller.timetable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.TimeTable;
import nl.rug.www.summerschool.model.TimeTableWeek;
import nl.rug.www.summerschool.networking.NetworkingService;

/**
 * This class is time table fragment on main pager activity.
 * It shows days of week from Monday, ... Sunday.
 * Each of day is a expandable drawer, so after clicking each item, it shows details of the day.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class TimeTableFragment extends Fragment {

    private RecyclerView mTimeTableRecyclerView;
    private TimeTableExpandableAdapter mTimeTableExpandableAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchTimeTablesTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        TextView section = (TextView)view.findViewById(R.id.section_name);
        section.setText("Time Table");

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchTimeTablesTask().execute();
            }
        });

        mTimeTableRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mTimeTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (mTimeTableExpandableAdapter == null) {
            mTimeTableExpandableAdapter = new TimeTableExpandableAdapter(getActivity(), generateTimeTableWeek());
            mTimeTableExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
            mTimeTableExpandableAdapter.setParentClickableViewAnimationDuration(ExpandableRecyclerAdapter.DEFAULT_ROTATE_DURATION_MS);
            mTimeTableExpandableAdapter.setParentAndIconExpandOnClick(true);
        } else {
            mTimeTableExpandableAdapter.notifyDataSetChanged();
        }

        mTimeTableRecyclerView.setAdapter(mTimeTableExpandableAdapter);
    }

    private class TimeTableParentViewHolder extends ParentViewHolder {

        private TextView mTimeTableTitleTextView;
        private ImageButton mParentDropDownArrow;

        public TimeTableParentViewHolder(View itemView) {
            super(itemView);

            mTimeTableTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_timetable_text_view);
            mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
        }
    }

    private class TimeTableChildViewHolder extends ChildViewHolder {

        private TextView mTimeTextView;
        private TextView mSubjectTextView;

        public TimeTableChildViewHolder(View itemView) {
            super(itemView);

            mTimeTextView = (TextView) itemView.findViewById(R.id.time_text_view);
            mSubjectTextView = (TextView) itemView.findViewById(R.id.subject_description);
        }
    }

    private class TimeTableExpandableAdapter extends ExpandableRecyclerAdapter<TimeTableParentViewHolder, TimeTableChildViewHolder> {

        private LayoutInflater mInflater;

        public TimeTableExpandableAdapter(Context context, List<ParentObject> parentItemList) {
            super(context, parentItemList);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public TimeTableParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_timetable_parent, viewGroup, false);
            return new TimeTableParentViewHolder(view);
        }

        @Override
        public TimeTableChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_timetable_child, viewGroup, false);
            return new TimeTableChildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(TimeTableParentViewHolder timeTableParentViewHolder, int i, Object o) {
            TimeTableWeek timeTableWeek = (TimeTableWeek)o;
            timeTableParentViewHolder.mTimeTableTitleTextView.setText(timeTableWeek.getDayOfWeek());
        }

        @Override
        public void onBindChildViewHolder(TimeTableChildViewHolder timeTableChildViewHolder, int i, Object o) {
            TimeTable timeTable = (TimeTable)o;
            timeTableChildViewHolder.mSubjectTextView.setText(timeTable.getTitle());
            String[] startParts = timeTable.getStartDate().split("T");
            String[] startParts2 = startParts[1].split(":");
            String[] endParts = timeTable.getEndDate().split("T");
            String[] endParts2 = endParts[1].split(":");
            timeTableChildViewHolder.mTimeTextView.
                    setText(startParts2[0]+":"+startParts2[1]+" - "+endParts2[0]+":"+endParts2[1]);
        }
    }

    private ArrayList<ParentObject> generateTimeTableWeek() {
        ContentsLab contentsLab = ContentsLab.get(getActivity());
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        List<TimeTableWeek> timeTableWeeks = contentsLab.getTimeTableWeeks();
        for (TimeTableWeek t : timeTableWeeks) {
            parentObjects.add(t);
        }
        return parentObjects;
    }

    private class FetchTimeTablesTask extends AsyncTask<Void, Void, List<TimeTable>> {

        List<TimeTable> mItems;

        @Override
        protected List<TimeTable> doInBackground(Void... params) {
            return new NetworkingService().fetchTimeTables();
        }

        @Override
        protected void onPostExecute(List<TimeTable> timeTables) {
            mItems = timeTables;
            ContentsLab.get(getActivity()).updateTimeTables(mItems);
            updateUI();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

}
