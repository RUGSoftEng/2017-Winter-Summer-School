package nl.rug.www.rugsummerschools.model;

import java.util.Date;

/**
 * This class is a model of time table.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
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
