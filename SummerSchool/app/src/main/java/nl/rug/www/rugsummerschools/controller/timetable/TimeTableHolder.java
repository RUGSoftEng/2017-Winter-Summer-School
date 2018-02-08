package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;


public class TimeTableHolder extends RecyclerView.ViewHolder{

    private static final String TAG = "TimeTableHolder";

    // TODO : Recyclerview with events

    private EventsPerDay mEventsPerDay;
    private LinearLayout mLinearLayout;
    private TextView mYearTextView;
    private TextView mWeekTextView;
    private TextView mDateTextView;
    private TextView mDayTextView;
    private RecyclerView mRecyclerView;

    private Context mContext;

    public TimeTableHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.item_timetable, parent, false));
        mContext = context;
        mLinearLayout = itemView.findViewById(R.id.linear_layout_for_a_day);
        mYearTextView = itemView.findViewById(R.id.year_month_text_view);
        mWeekTextView = itemView.findViewById(R.id.week_text_view);
        mDateTextView = itemView.findViewById(R.id.date_text_view);
        mDayTextView = itemView.findViewById(R.id.day_text_view);
        mRecyclerView = itemView.findViewById(R.id.date_recycler_view);
    }

    public void bind(EventsPerDay eventsPerDay) {
        mEventsPerDay = eventsPerDay;
        Calendar calendar = Calendar.getInstance();
        Date date = mEventsPerDay.getDate();
        calendar.setTime(date);
        mYearTextView.setVisibility(View.GONE);
        mWeekTextView.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.GONE);
        int d = calendar.get(Calendar.DATE);
        if (d == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MMM", Locale.getDefault());
            mYearTextView.setText(sdf.format(date));
            mYearTextView.setVisibility(View.VISIBLE);
        }
        if (Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
            String week = "";
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
            week += sdf.format(date);
            week += " - ";
            calendar.add(Calendar.DATE, 6);
            week += sdf.format(calendar.getTime());
            mWeekTextView.setText(week);
            mWeekTextView.setVisibility(View.VISIBLE);
        }
        if (mEventsPerDay.getEvents().size() != 0) {
            mLinearLayout.setVisibility(View.VISIBLE);
            calendar.setTime(date);
            mDateTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
            SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.getDefault());
            mDayTextView.setText(sdf.format(date));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(new EventAdapter(mEventsPerDay.getEvents(), mContext));
        }
    }
}
