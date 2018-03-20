package nl.rug.www.rugsummerschools.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is a model of events per day
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 13/04/2017
 **/

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
