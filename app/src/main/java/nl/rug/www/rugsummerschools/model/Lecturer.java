package nl.rug.www.rugsummerschools.model;

/**
 * This class is a model of lecturer information that contains
 *
 * @author Jeongkyun
 * @version 2.0.0
 * @since 13/04/2017
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
