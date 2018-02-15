package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.model.EventsPerDay;

/**
 * ViewAdapter class for timetable recycler view.
 * Timetable consists of list of events per day which has list of events.
 * It binds list of events per day to the view holder.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 10/02/2018
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableHolder> {

    private Context mContext;
    private List<EventsPerDay> mEventsPerDayList;

    public TimeTableAdapter(List<EventsPerDay> eventsPerDays, Context context) {
        mContext = context;
        mEventsPerDayList = eventsPerDays;
    }

    @Override
    public TimeTableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new TimeTableHolder(layoutInflater, parent, mContext);
    }

    @Override
    public void onBindViewHolder(TimeTableHolder holder, int position) {
        EventsPerDay eventsPerDay = mEventsPerDayList.get(position);
        holder.bind(eventsPerDay);
    }

    @Override
    public int getItemCount() {
        return mEventsPerDayList.size();
    }
}
