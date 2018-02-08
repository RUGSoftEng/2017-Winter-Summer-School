package nl.rug.www.rugsummerschools.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is a model of time table for a week
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class EventsPerDay {

    private Date mDate;
    private List<Event> mEvents;

    public EventsPerDay(Date date) {
        mDate = date;
        mEvents = new ArrayList<>();
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }

    public Date getDate() {
        return mDate;
    }

}
