package nl.rug.www.rugsummerschool.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a model of time table for a week
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class EventsPerDay implements ParentObject {

    private String mDayOfWeek;
    private List<Object> mChildrenList;

    public EventsPerDay(String dayOfWeek) {
        mDayOfWeek = dayOfWeek;
        mChildrenList = new ArrayList<>();
    }

    public String getDayOfWeek() {
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
