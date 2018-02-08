package nl.rug.www.rugsummerschools.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 5/14/17.
 */

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
