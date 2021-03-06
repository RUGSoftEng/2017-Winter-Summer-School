package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.model.Event;

/**
 * ViewAdapter class for event recycler view.
 * It binds list of events to the view holder.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 10/02/2018
 */

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {

    private List<Event> mEvents;
    private Context mContext;

    public EventAdapter(List<Event> events, Context context) {
        mEvents = events;
        mContext = context;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new EventHolder(layoutInflater, parent, mContext);
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
