package nl.rug.www.rugsummerschool.model;

/**
 * Created by jk on 5/14/17.
 */

public class ForumComment {

    private String mPosterId;
    private String mPoster;
    private String mDate;
    private String mText;

    public String getPosterId() {
        return mPosterId;
    }

    public void setPosterId(String posterId) {
        mPosterId = posterId;
    }

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

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
