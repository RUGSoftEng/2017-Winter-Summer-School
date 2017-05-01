package nl.rug.www.summerschool.model;

import android.graphics.drawable.Drawable;

/**
 * This class is a model of lecturer information that contains
 *
 * @since 13/04/2017
 * @author Jeongkyun
 */

public class Lecturer extends Content {

    private Drawable mProfilePicture;

    public Drawable getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(Drawable profilePicture) {
        mProfilePicture = profilePicture;
    }
}
