package nl.rug.www.rugsummerschools.controller.timetable;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Event;

/**
 * This class is to show detail view for a event.
 *
 * @since 10/02/2018
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String EXTRA_EVENT_ID = "nl.rug.www.rugsummerschool.event_id";

    private Event mEvent;
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    public static Intent newIntent(Context packageContext, String content) {
        Intent intent = new Intent(packageContext, EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_detail);

        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
        mEvent = ContentsLab.get().getEvent(eventId);
        String title = mEvent.getTitle();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView timeTextView = findViewById(R.id.event_time);
        Date startDate = new DateTime(mEvent.getStartDate()).toDate();
        Date endDate = new DateTime(mEvent.getEndDate()).toDate();
        SimpleDateFormat parseDate = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timePeriod = parseTime.format(startDate) + " - " + parseTime.format(endDate);
        timeTextView.setText(timePeriod);

        TextView locationTextView = findViewById(R.id.event_location);
        locationTextView.setText(mEvent.getLocation());

        TextView detailTextView = findViewById(R.id.event_detail);
        detailTextView.setText(mEvent.getDescription());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(mEvent.getLocation(), 1);
            LatLng location = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

            mGoogleMap.addMarker(new MarkerOptions().position(location));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
