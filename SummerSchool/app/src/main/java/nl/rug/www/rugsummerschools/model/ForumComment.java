package nl.rug.www.rugsummerschools.model;

/**
 * Created by jk on 5/14/17.
 */

public class ForumComment extends Content {

    private String mPosterId;
    private String mPoster;
    private String mDate;
    private String mImgUrl;

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

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
}
