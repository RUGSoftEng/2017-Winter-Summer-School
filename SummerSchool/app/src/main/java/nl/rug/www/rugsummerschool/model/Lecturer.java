package nl.rug.www.rugsummerschool.model;

import android.graphics.drawable.Drawable;

/**
 * This class is a model of lecturer information that contains
 *
 * @since 13/04/2017
 * @author Jeongkyun
 */

public class Lecturer extends Content {

    private Drawable mProfilePicture;
    private String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Drawable getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(Drawable profilePicture) {
        mProfilePicture = profilePicture;
    }
}
