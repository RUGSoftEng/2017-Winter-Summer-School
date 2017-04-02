package nl.rug.www.summerschool;

/**
 * Created by jk on 3/31/17.
 */

public class Announcement extends Content {
    private String mPoster;
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
}
