package nl.rug.www.summerschool.model;

import android.graphics.drawable.Drawable;

import nl.rug.www.summerschool.model.Content;

/**
 * This class is a model of lecturer information that contains
 *
 * @since 13/04/2017
 * @author Jeongkyun
 */

public class Lecturer extends Content {

    private Drawable mProfilePicture;
    private String mDepartment;
    private String mImagePath;

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public Drawable getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(Drawable profilePicture) {
        mProfilePicture = profilePicture;
    }
}
