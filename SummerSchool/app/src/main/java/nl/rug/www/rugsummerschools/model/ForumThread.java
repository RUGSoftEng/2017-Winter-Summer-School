package nl.rug.www.rugsummerschools.model;

import java.util.List;

/**
 * Forum thread model
 *
 * @since 14/05/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 **/

public class ForumThread extends Content {

    private String mDate;
    private String mPosterId;
    private String mPoster;
    private String mImgUrl;
    private List<String> mForumComments;

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
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

    public List<String> getForumComments() {
        return mForumComments;
    }

    public void setForumComments(List<String> forumComments) {
        mForumComments = forumComments;
    }
}
