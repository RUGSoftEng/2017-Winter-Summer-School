package nl.rug.www.rugsummerschools.controller.announcement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.Announcement;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

/**
 * Created by jk on 17. 12. 6.
 */

public abstract class AnnouncementHolder extends ContentHolder<Announcement> implements View.OnClickListener{

    private TextView mInitialView;
    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mNewTextView;
    private TextView mRelativeTimeView;

    public AnnouncementHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_announcement, parent, false));

        mInitialView = (TextView)itemView.findViewById(R.id.initial_text_view);
        mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
        mAuthorTextView = (TextView)itemView.findViewById(R.id.author_text_view);
        mNewTextView = (TextView)itemView.findViewById(R.id.new_image_view);
        mRelativeTimeView = (TextView)itemView.findViewById(R.id.time_stamp);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(Announcement announcement) {
        mContent = announcement;
        mTitleTextView.setText(mContent.getTitle());
        String poster = mContent.getPoster();
        String initial = poster.toUpperCase().charAt(0) + "";
        mInitialView.setText(initial);
        GradientDrawable circle = (GradientDrawable)mInitialView.getBackground();
        circle.setColor(mContent.getColor());
        String byPoster = "By " + poster;
        mAuthorTextView.setText(byPoster);
        Date date = new DateTime(mContent.getDate()).toDate();
        mRelativeTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
    }
}