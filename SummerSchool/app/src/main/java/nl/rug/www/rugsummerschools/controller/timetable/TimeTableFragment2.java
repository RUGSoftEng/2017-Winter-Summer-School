package nl.rug.www.rugsummerschools.controller.timetable;

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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;


public class TimeTableFragment2 extends Fragment {

    private static final String TAG = "TimeTableFragment";

    private List<EventsPerDay> mEventsPerDayList;

    private RecyclerView mRecyclerView;

//    private void createMockData() {
//        List<EventsPerDay> events = new ArrayList<>();
//        EventsPerDay firstdate = new EventsPerDay();
//        Event e1 = new Event();
//        e1.setTitle("New Year");
//        e1.setDescription("2018 New year is just started!");
//        e1.setStartDate("2018-01-01T00:00:00.000Z");
//        e1.setEndDate("2018-01-01T00:00:00.000Z");
//        e1.setLocation("Zernike Campus");
//
//        ContentsLab.get().updateTimeTableWeeks(events);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initializeCalendar();
        } catch (ParseException e) {
            Log.e(TAG, "Cannot do initialize calendar!");
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        TextView sectionName = view.findViewById(R.id.section_name);
        sectionName.setText(R.string.time_table);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new TimeTableAdapter(mEventsPerDayList, getActivity()));

        Calendar mStartDay = Calendar.getInstance();
        mStartDay.set(Calendar.DAY_OF_MONTH, 1);
        mStartDay.set(Calendar.MONTH, 0);
        mStartDay.set(Calendar.YEAR, 2010);

        Calendar today = Calendar.getInstance();

        long diff = (today.getTimeInMillis() - mStartDay.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        mRecyclerView.scrollToPosition((int) diff);
        return view;
    }

    private void initializeCalendar() throws ParseException {
        mEventsPerDayList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date startDate = formatter.parse("2010-01-01");
        Date endDate = formatter.parse("2020-01-01");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime() ) {
            EventsPerDay newEventsPerDay = new EventsPerDay(date);
            mEventsPerDayList.add(newEventsPerDay);
        }
    }
}
