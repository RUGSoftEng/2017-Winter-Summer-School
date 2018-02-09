package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.Event;


public class EventHolder extends ContentHolder<Event> implements View.OnClickListener{

    private Event mEvent;
    private Context mContext;
    private TextView mTitleTextView;
    private TextView mTimeTextView;
    private TextView mLocationTextView;

    public EventHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.item_event, parent, false));
        mContext = context;
        mTitleTextView = itemView.findViewById(R.id.title_text_view);
        mTimeTextView = itemView.findViewById(R.id.time_text_view);
        mLocationTextView = itemView.findViewById(R.id.location_text_view);
        itemView.setOnClickListener(this);
    }

    public void bind(Event event) {
        mEvent = event;
        mTitleTextView.setText(mEvent.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String time = sdf.format(mEvent.getStartDate()) + " - " + sdf.format(mEvent.getEndDate());
        mTimeTextView.setText(time);
        mLocationTextView.setText(mEvent.getLocation());
    }

    @Override
    public void onClick(View view) {
        Intent intent = TimetableDetailActivity.newIntent(mContext, mEvent.getId());
        mContext.startActivity(intent);
    }
}
