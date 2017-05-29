package nl.rug.www.rugsummerschool.model;

/**
 * This class is a model of time table.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class Event extends Content {

    private String mStartDate;
    private String mEndDate;

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }
}
