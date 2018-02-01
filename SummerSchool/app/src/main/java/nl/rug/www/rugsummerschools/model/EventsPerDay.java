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
    private List<Object> mChildrenList;

    public EventsPerDay(Date dayOfWeek) {
        mDayOfWeek = dayOfWeek;
        mChildrenList = new ArrayList<>();
    }

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
