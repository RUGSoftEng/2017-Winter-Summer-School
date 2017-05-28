package nl.rug.www.rugsummerschool.model;

import java.util.List;

/**
 * Created by jk on 5/14/17.
 */

public class ForumThread extends Content {

    private String mDate;
    private String mPosterId;
    private String mPoster;
    private List<ForumComment> mForumCommentList;

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
