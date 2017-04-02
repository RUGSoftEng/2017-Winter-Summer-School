package nl.rug.www.summerschool;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jk on 4/1/17.
 */

public class TimeTableWeek implements ParentObject {

    private String mDayOfWeek;
    private List<Object> mChildrenList;

    public TimeTableWeek(String dayOfWeek) {
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
