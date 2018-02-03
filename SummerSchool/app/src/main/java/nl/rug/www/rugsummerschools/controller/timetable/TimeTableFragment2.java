package nl.rug.www.rugsummerschools.controller.timetable;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Content;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;
import nl.rug.www.rugsummerschools.networking.NetworkingService;


public class TimeTableFragment2 extends Fragment {

    private static final String TAG = "TimeTableFragment";

    private List<EventsPerDay> mEventsPerDayList;

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        TextView sectionName = view.findViewById(R.id.section_name);
        sectionName.setText(R.string.time_table);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new FetchEvents().execute();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new TimeTableAdapter(mEventsPerDayList, getActivity()));
            Calendar mStartDay = Calendar.getInstance();
            mStartDay.set(Calendar.DAY_OF_MONTH, 1);
            mStartDay.set(Calendar.MONTH, 0);
            mStartDay.set(Calendar.YEAR, 2010);

            Calendar today = Calendar.getInstance();

            long diff = (today.getTimeInMillis() - mStartDay.getTimeInMillis()) / (24 * 60 * 60 * 1000);
            mRecyclerView.scrollToPosition((int) diff);
        }
    }

    private void initializeCalendar(List<Event> events) throws ParseException {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.getStartDate().compareTo(t1.getStartDate());
            }
        });
        mEventsPerDayList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date startDate = formatter.parse("2010-01-01");
        Date endDate = formatter.parse("2020-01-01");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int idx = 0;
        Event e;
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime() ) {

            EventsPerDay newEventsPerDay = new EventsPerDay(date);
            List<Event> eventList = new ArrayList<>();
            while (idx < events.size() && date.compareTo(events.get(idx).getStartDate()) > 0) {
                eventList.add(events.get(idx));
                idx++;
            }

            if (eventList.size() != 0)
                mEventsPerDayList.get(mEventsPerDayList.size() - 1).setEvents(eventList);
            mEventsPerDayList.add(newEventsPerDay);
        }
    }

    private class FetchEvents extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Void... voids) {
            String school = ContentsLab.get().getSchoolId();
            return new NetworkingService<Event>().fetchData(NetworkingService.EVENT, school);
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            try {
                initializeCalendar(events);
                setupAdapter();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
