package nl.rug.www.rugsummerschools.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is a model of time table for a week
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class EventsPerDay implements ParentObject {

    private Date mDayOfWeek;
    private List<Event> mEvents;

    public EventsPerDay(Date date) {
        mDayOfWeek = date;
        mEvents = new ArrayList<>();
        mChildrenList = new ArrayList<>();
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }

    private List<Object> mChildrenList;

    public Date getDayOfWeek() {
        return mDayOfWeek;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }
}
