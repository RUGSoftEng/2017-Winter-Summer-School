package nl.rug.www.rugsummerschool.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * This class is a model of lecturer information that contains
 *
 * @since 13/04/2017
 * @author Jeongkyun
 */

public class Lecturer extends Content {

    private String imgurl;
    private String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
