package nl.rug.www.rugsummerschools.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 5/14/17.
 */

public class ForumThread extends Content implements ParentObject {

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

    private List<Object> commentToObject(List<ForumComment> comments) {
        List<Object> objects = new ArrayList<>();
        for (ForumComment fc : comments) {
            objects.add(fc);
        }
        return objects;
    }

    @Override
    public List<Object> getChildObjectList() {
        return commentToObject(mForumCommentList);
    }

    @Override
    public void setChildObjectList(List<Object> list) {
    }
}
