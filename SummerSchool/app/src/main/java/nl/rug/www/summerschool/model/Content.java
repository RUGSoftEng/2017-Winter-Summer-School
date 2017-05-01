package nl.rug.www.summerschool.model;

import java.util.Date;
import java.util.UUID;

/**
 * Content is a class of model that contains minimum attributes of a content
 *
 * @since 08/04/2017
 * @author Jeongkyun Oh
 */
public class Content {

    /** unique identification number */
    private String mId;

    /** title of content */
    private String mTitle;

    /** description of content */
    private String mDescription;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

}
