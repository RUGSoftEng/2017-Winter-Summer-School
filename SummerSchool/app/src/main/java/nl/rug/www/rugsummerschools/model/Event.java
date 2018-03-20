package nl.rug.www.rugsummerschools.model;

import java.util.Date;

/**
 * This class is a model of event.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public class Event extends Content {

    private String mSchool;
    private Date mStartDate;
    private Date mEndDate;
    private String mLocation;

    public String getSchool() {
        return mSchool;
    }

    public void setSchool(String school) {
        mSchool = school;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
