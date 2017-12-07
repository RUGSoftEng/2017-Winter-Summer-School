package nl.rug.www.rugsummerschools.model;

import android.graphics.Color;

/**
 * Announcement class is a model of contents that contains author and date that is posted.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class Announcement extends Content {

    /** name of person who upload the announcement */
    private String mPoster;

    /** date that the announcement is posted */
    private String mDate;

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getColor() {
        int hash = mPoster.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return Color.rgb(r, g, b);
    }

}
