package nl.rug.www.rugsummerschools.controller.timetable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This fragment is to show list of events per day that inflates main activity.
 * It filter 1st of month to show monthly background and start of week to show week view.
 *
 * @since 10/02/2018
 * @author Jeongkyun Oh
 * @version 2.0.0
 */
public class TimeTableFragment extends Fragment {

    private static final String TAG = "TimeTableFragment";

    private List<EventsPerDay> mEventsPerDayList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mTodayOffset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        TextView sectionName = view.findViewById(R.id.section_name);
        sectionName.setText(R.string.time_table);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        new FetchEvents().execute();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_timetable, menu);
        Log.i(TAG, "Today manu is inflated");
    }

    private void scrollToToday() {
        ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mTodayOffset, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today_menu :
                scrollToToday();
                return true;
            case R.id.refresh_menu :
                new FetchEvents().execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new TimeTableAdapter(mEventsPerDayList, getActivity()));
            scrollToToday();
        }
    }

    private void initializeCalendar(List<Event> events) throws ParseException {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.getStartDate().compareTo(t1.getStartDate());
            }
        });
        Log.d(TAG, events.toString());
        mEventsPerDayList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date startDate = formatter.parse("2010-01-01");
        Date endDate = formatter.parse("2030-01-01");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        constructEventsList(events, start, end);
    }

    private void constructEventsList(List<Event> events, Calendar start, Calendar end) {
        int idx = 0;
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime() ) {

            EventsPerDay newEventsPerDay = new EventsPerDay(date);
            List<Event> eventList = new ArrayList<>();
            while (idx < events.size() && date.compareTo(events.get(idx).getStartDate()) > 0) {
                eventList.add(events.get(idx));
                idx++;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (calendar.get(Calendar.DATE) == 1 || Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
                mEventsPerDayList.add(newEventsPerDay);
                if (today.after(calendar))
                    mTodayOffset++;
            } else if (eventList.size() != 0) {
                newEventsPerDay.setEvents(eventList);
                mEventsPerDayList.add(newEventsPerDay);
                if (today.after(calendar))
                    mTodayOffset++;
            }
        }
    }

    private class FetchEvents extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Event> doInBackground(Void... voids) {
            mTodayOffset = -1;
            String school = ContentsLab.get().getSchoolId();
            return new NetworkingService<Event>().fetchData(NetworkingService.EVENT, school);
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        protected void onPostExecute(final List<Event> events) {
            super.onPostExecute(events);
            ContentsLab.get().updateEvents(events);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        initializeCalendar(events);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    setupAdapter();
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }.execute();
        }
    }
}
