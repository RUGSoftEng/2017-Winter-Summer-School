package nl.rug.www.rugsummerschools.controller.announcement;

import android.graphics.drawable.GradientDrawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Date;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.ContentsLab;

/**
 * ViewHolder class for announcement recycler view.
 * Its role is to hook up model with view
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 06/12/17
 */

public abstract class AnnouncementHolder extends ContentHolder<Announcement> implements View.OnClickListener {

    private TextView mInitialView;
    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mRelativeTimeView;

    public AnnouncementHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_announcement, parent, false));

        mInitialView = itemView.findViewById(R.id.initial_text_view);
        mTitleTextView = itemView.findViewById(R.id.content_title);
        mAuthorTextView = itemView.findViewById(R.id.author_text_view);
        mRelativeTimeView = itemView.findViewById(R.id.time_stamp);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(Announcement announcement) {
        mContent = announcement;
        mContent.setPoster(ContentsLab.get().getSchoolInfo().getSchoolName());
        mTitleTextView.setText(mContent.getTitle());
        String poster = mContent.getPoster();
        mInitialView.setText(mContent.getInitial());
        GradientDrawable circle = (GradientDrawable) mInitialView.getBackground();
        circle.setColor(mContent.getColor());
        String byPoster = "By " + poster;
        mAuthorTextView.setText(byPoster);
        Date date = new DateTime(mContent.getDate()).toDate();

        mRelativeTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
    }
}