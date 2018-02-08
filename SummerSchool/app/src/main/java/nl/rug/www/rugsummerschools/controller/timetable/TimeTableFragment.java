package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is time table fragment on main pager activity.
 * It shows days of week from Monday, ... Sunday.
 * Each of day is a expandable drawer, so after clicking each item, it shows details of the day.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

@Deprecated
public class TimeTableFragment extends Fragment {

    private RecyclerView mTimeTableRecyclerView;
    private TimeTableExpandableAdapter mTimeTableExpandableAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PREVIOUS = -1;
    private static final int CURRENT = 0;
    private static final int NEXT = 1;
    private int mWeek = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        TextView section = (TextView)view.findViewById(R.id.section_name);
        section.setText(R.string.time_table);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchTimeTablesTask(CURRENT, mWeek).execute();
                new FetchTimeTablesTask(PREVIOUS, mWeek-1).execute();
                new FetchTimeTablesTask(NEXT, mWeek+1).execute();
                setupAdapter();
            }
        });

//        Button previousWeekButton = (Button)view.findViewById(R.id.previous_week_button);
//        previousWeekButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWeek--;
//                ContentsLab contentsLab = ContentsLab.get();
//                contentsLab.updateNextWeekTimeTable(contentsLab.getCurrentWeekEvents());
//                contentsLab.updateTimeTableWeeks(contentsLab.getPreviousWeekEvents());
//                new FetchTimeTablesTask(PREVIOUS, mWeek-1).execute();
//                setupAdapter();
//            }
//        });

//        Button nextWeekButton = (Button)view.findViewById(R.id.next_week_button);
//        nextWeekButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWeek++;
//                ContentsLab contentsLab = ContentsLab.get();
//                contentsLab.updatePreviousWeekTimeTable(contentsLab.getCurrentWeekEvents());
//                contentsLab.updateTimeTableWeeks(contentsLab.getNextWeekEvents());
//                new FetchTimeTablesTask(NEXT, mWeek+1).execute();
//                setupAdapter();
//            }
//        });

        mTimeTableRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mTimeTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new FetchTimeTablesTask(CURRENT, mWeek).execute();
        new FetchTimeTablesTask(PREVIOUS, mWeek-1).execute();
        new FetchTimeTablesTask(NEXT, mWeek+1).execute();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mTimeTableExpandableAdapter = new TimeTableExpandableAdapter(getActivity(), generateTimeTableWeek());
            mTimeTableExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
            mTimeTableExpandableAdapter.setParentClickableViewAnimationDuration(ExpandableRecyclerAdapter.DEFAULT_ROTATE_DURATION_MS);
            mTimeTableExpandableAdapter.setParentAndIconExpandOnClick(true);
            mTimeTableRecyclerView.setAdapter(mTimeTableExpandableAdapter);
        }
    }

    private class TimeTableParentViewHolder extends ParentViewHolder {

        private TextView mTimeTableTitleTextView;

        private TimeTableParentViewHolder(View itemView) {
            super(itemView);

            mTimeTableTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_timetable_text_view);
        }
    }

    private class TimeTableChildViewHolder extends ChildViewHolder {

        private TextView mTimeTextView;
        private TextView mSubjectTextView;

        private TimeTableChildViewHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time_text_view);
            mSubjectTextView = (TextView) itemView.findViewById(R.id.subject_description);
        }
    }

    private class TimeTableExpandableAdapter extends ExpandableRecyclerAdapter<TimeTableParentViewHolder, TimeTableChildViewHolder> {

        private LayoutInflater mInflater;

        private TimeTableExpandableAdapter(Context context, List<ParentObject> parentItemList) {
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
            EventsPerDay eventsPerDay = (EventsPerDay)o;
//            timeTableParentViewHolder.mTimeTableTitleTextView.setText(eventsPerDay.getDate());
        }

        @Override
        public void onBindChildViewHolder(TimeTableChildViewHolder timeTableChildViewHolder, int i, Object o) {
            final Event event = (Event)o;
            timeTableChildViewHolder.mSubjectTextView.setText(event.getTitle());
            Date start = new DateTime(event.getStartDate()).toDate();

            Date end = new DateTime(event.getEndDate()).toDate();
            SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.getDefault());

            timeTableChildViewHolder.mTimeTextView.
                    setText(time.format(start)+" - "+time.format(end));

            timeTableChildViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View popupView = inflater.inflate(R.layout.pop_up_window_time_table, null);
                    ImageView closeView = (ImageView)popupView.findViewById(R.id.close_button);
                    TextView locView = (TextView)popupView.findViewById(R.id.popup_location);
                    locView.setText(event.getLocation());
                    TextView detailView = (TextView)popupView.findViewById(R.id.popup_detail);
                    detailView.setText(event.getDescription());
                    final PopupWindow popup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        popup.setElevation(0.5f);
                    }
                    popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    closeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                        }
                    });

                }
            });
        }
    }

    private ArrayList<ParentObject> generateTimeTableWeek() {
        ContentsLab contentsLab = ContentsLab.get();
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        List<EventsPerDay> eventsPerDays = contentsLab.getCurrentWeekEvents();
        for (EventsPerDay t : eventsPerDays) {
//            parentObjects.add(t);
        }
        return parentObjects;
    }

    private class FetchTimeTablesTask extends AsyncTask<Void, Void, List<EventsPerDay>> {

        List<EventsPerDay> mItems;
        private int week;
        private int pressedButton;

        public FetchTimeTablesTask(int pressedButton, int week) {
            this.pressedButton = pressedButton;
            this.week = week;
        }

        @Override
        protected List<EventsPerDay> doInBackground(Void... params) {
//            return new NetworkingService().fetchTimeTables(this.week);
            return null;
        }

        @Override
        protected void onPostExecute(List<EventsPerDay> timeTables) {
            mItems = timeTables;
            switch (pressedButton) {
                case PREVIOUS :
                    ContentsLab.get().updatePreviousWeekTimeTable(mItems);
                    break;
                case CURRENT :
                    ContentsLab.get().updateTimeTableWeeks(mItems);
                    setupAdapter();
                    break;
                case NEXT :
                    ContentsLab.get().updateNextWeekTimeTable(mItems);
                    break;
            }

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

}
