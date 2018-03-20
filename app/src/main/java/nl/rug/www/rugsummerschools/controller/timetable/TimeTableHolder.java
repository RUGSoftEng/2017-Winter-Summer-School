package nl.rug.www.rugsummerschools.controller.timetable;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.EventsPerDay;

/**
 * ViewHolder class for timetable recycler view.
 * It binds a eventsPerDay to the associated view.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 10/02/2018
 */

public class TimeTableHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "TimeTableHolder";

    private EventsPerDay mEventsPerDay;
    private FrameLayout mFrameLayout;
    private LinearLayout mLinearLayout;
    private ImageView mYearImageView;
    private TextView mYearTextView;
    private TextView mWeekTextView;
    private TextView mDateTextView;
    private TextView mDayTextView;
    private RecyclerView mRecyclerView;

    private Context mContext;

    public TimeTableHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.item_timetable, parent, false));
        mContext = context;
        mFrameLayout = itemView.findViewById(R.id.year_frame_layout);
        mLinearLayout = itemView.findViewById(R.id.linear_layout_for_a_day);
        mYearImageView = itemView.findViewById(R.id.year_background);
        mYearTextView = itemView.findViewById(R.id.year_month_text_view);
        mWeekTextView = itemView.findViewById(R.id.week_text_view);
        mDateTextView = itemView.findViewById(R.id.date_text_view);
        mDayTextView = itemView.findViewById(R.id.day_text_view);
        mRecyclerView = itemView.findViewById(R.id.date_recycler_view);
    }

    private void restrictFirstday(Calendar calendar, Date date) {
        if (calendar.get(Calendar.DATE) == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MMMM", Locale.getDefault());
            SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
            setMonthBackground(Integer.valueOf(month.format(date)));
            mYearTextView.setText(sdf.format(date));
            mFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    private void restrictStartOfWeek(Calendar calendar, Date date) {
        if (Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
            String week = "";
            SimpleDateFormat sdfm = new SimpleDateFormat("MMM", Locale.getDefault());
            SimpleDateFormat sdfd = new SimpleDateFormat("d", Locale.getDefault());
            String startMonth = sdfm.format(date);
            week += startMonth;
            week += " " + sdfd.format(date);
            week += " - ";
            calendar.add(Calendar.DATE, 6);
            String endMonth = sdfm.format(calendar.getTime());
            if (!startMonth.equals(endMonth))
                week += endMonth;
            week += " " + sdfd.format(calendar.getTime());
            mWeekTextView.setText(week);
            mWeekTextView.setVisibility(View.VISIBLE);
        }
    }

    private void restrictNoItem(Calendar calendar, Date date) {
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

    public void bind(EventsPerDay eventsPerDay) {
        mEventsPerDay = eventsPerDay;
        Calendar calendar = Calendar.getInstance();
        Date date = mEventsPerDay.getDate();
        calendar.setTime(date);
        mFrameLayout.setVisibility(View.GONE);
        mWeekTextView.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.GONE);
        restrictFirstday(calendar, date);
        restrictStartOfWeek(calendar, date);
        restrictNoItem(calendar, date);
    }

    private void setMonthBackground(int month) {
        switch (month) {
            case 1:
                Glide.with(mContext).load(R.drawable.bkg_01_january).into(mYearImageView);
                break;
            case 2:
                Glide.with(mContext).load(R.drawable.bkg_02_february).into(mYearImageView);
                break;
            case 3:
                Glide.with(mContext).load(R.drawable.bkg_03_march).into(mYearImageView);
                break;
            case 4:
                Glide.with(mContext).load(R.drawable.bkg_04_april).into(mYearImageView);
                break;
            case 5:
                Glide.with(mContext).load(R.drawable.bkg_05_may).into(mYearImageView);
                break;
            case 6:
                Glide.with(mContext).load(R.drawable.bkg_06_june).into(mYearImageView);
                break;
            case 7:
                Glide.with(mContext).load(R.drawable.bkg_07_july).into(mYearImageView);
                break;
            case 8:
                Glide.with(mContext).load(R.drawable.bkg_08_august).into(mYearImageView);
                break;
            case 9:
                Glide.with(mContext).load(R.drawable.bkg_09_september).into(mYearImageView);
                break;
            case 10:
                Glide.with(mContext).load(R.drawable.bkg_10_october).into(mYearImageView);
                break;
            case 11:
                Glide.with(mContext).load(R.drawable.bkg_11_november).into(mYearImageView);
                break;
            case 12:
                Glide.with(mContext).load(R.drawable.bkg_12_december).into(mYearImageView);
                break;
            default:
                Glide.with(mContext).load(R.drawable.bkg_01_january).into(mYearImageView);

        }
    }
}
