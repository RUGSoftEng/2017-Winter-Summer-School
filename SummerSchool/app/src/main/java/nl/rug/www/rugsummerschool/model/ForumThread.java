package nl.rug.www.rugsummerschool.model;

import java.util.List;

/**
 * Created by jk on 5/14/17.
 */

public class ForumThread extends Content {

    private String mDate;
    private String mPosterId;
    private String mPoster;
    private String mImgUrl;
    private List<ForumComment> mForumCommentList;

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

    public List<ForumComment> getForumCommentList() {
        return mForumCommentList;
    }

    public void setForumCommentList(List<ForumComment> forumCommentList) {
        mForumCommentList = forumCommentList;
    }
}
