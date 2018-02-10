package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;

/**
 * This class is to show detail view for a event.
 *
 * @since 10/02/2018
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public class EventDetailActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "nl.rug.www.rugsummerschool.event_id";

    private Event mEvent;

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_detail);
        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
        mEvent = ContentsLab.get().getEvent(eventId);
        String title = mEvent.getTitle();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);


        TextView timeTextView = findViewById(R.id.event_time);
        Date startDate = new DateTime(mEvent.getStartDate()).toDate();
        Date endDate = new DateTime(mEvent.getEndDate()).toDate();
        SimpleDateFormat parseDate = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String timePeriod = parseTime.format(startDate) + " - " + parseTime.format(endDate);
        timeTextView.setText(timePeriod);

        TextView locationTextView = findViewById(R.id.event_location);
        locationTextView.setText(mEvent.getLocation());

        TextView detailTextView = findViewById(R.id.event_detail);
        detailTextView.setText(mEvent.getDescription());

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
