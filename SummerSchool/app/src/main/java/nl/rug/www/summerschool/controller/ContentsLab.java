package nl.rug.www.summerschool.controller;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.summerschool.model.Announcement;
import nl.rug.www.summerschool.model.GeneralInfo;
import nl.rug.www.summerschool.model.Lecturer;
import nl.rug.www.summerschool.model.TimeTable;
import nl.rug.www.summerschool.model.TimeTableWeek;

/**
 * ContentsLab stores data that fetched from the database.
 * Singleton pattern is used so that any class can access to the data.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class ContentsLab {

    /** unique instance of ContentsLab */
    private static ContentsLab sContentsLab;

    private ArrayList<Announcement> mAnnouncements;
    private ArrayList<GeneralInfo> mGeneralInfos;
    private ArrayList<TimeTable> mTimeTables;
    private ArrayList<TimeTableWeek> mTimeTableWeeks;
    private ArrayList<Lecturer> mLecturers;
    private ArrayList<String> mLogInData;

    public static ContentsLab get() {
        if (sContentsLab == null) {
            sContentsLab = new ContentsLab();
        }
        return sContentsLab;
    }

    public ArrayList<String> getmLogInData() {
        return mLogInData;
    }

    public void setmLogInData(ArrayList<String> mLogInData) {
        this.mLogInData = mLogInData;
    }

    private ContentsLab() {
        mAnnouncements = new ArrayList<>();
        mGeneralInfos = new ArrayList<>();
        mTimeTables = new ArrayList<>();

        mTimeTableWeeks = new ArrayList<>();
        mTimeTableWeeks.add(new TimeTableWeek("Monday"));
        mTimeTableWeeks.add(new TimeTableWeek("Tuesday"));
        mTimeTableWeeks.add(new TimeTableWeek("Wednesday"));
        mTimeTableWeeks.add(new TimeTableWeek("Thursday"));
        mTimeTableWeeks.add(new TimeTableWeek("Friday"));
        mTimeTableWeeks.add(new TimeTableWeek("Saterday"));
        mTimeTableWeeks.add(new TimeTableWeek("Sunday"));
        mLecturers = new ArrayList<>();
    }

    public List<GeneralInfo> getGeneralInfos() { return mGeneralInfos; }

    public GeneralInfo getGeneralInfo(String id) {
        for (GeneralInfo c : mGeneralInfos) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<Announcement> getAnnouncements() { return mAnnouncements; }

    public Announcement getAnnouncement(String id) {
        for (Announcement c : mAnnouncements) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<TimeTableWeek> getTimeTableWeeks() { return mTimeTableWeeks; }

    public List<Lecturer> getLecturers() { return mLecturers; }

    public Lecturer getLecturer(String id) {
        for (Lecturer c : mLecturers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public void updateAnnouncements(List<Announcement> announcements) {
        mAnnouncements = (ArrayList<Announcement>) announcements;
    }

    public void updateGeneralInfos(List<GeneralInfo> generalInfos) {
        mGeneralInfos = (ArrayList<GeneralInfo>) generalInfos;
    }

    public void updateTimeTables(List<TimeTable> timeTables) {
        mTimeTables = (ArrayList<TimeTable>) timeTables;
        createTimeTableWeek();
    }

    public void updateLecturers(List<Lecturer> lecturers) {
        mLecturers = (ArrayList<Lecturer>) lecturers;
    }

    private void createTimeTableWeek() {
        List<Object> monday = new ArrayList<>();
        List<Object> tuesday = new ArrayList<>();
        List<Object> wednesday = new ArrayList<>();
        List<Object> thursday = new ArrayList<>();
        List<Object> friday = new ArrayList<>();
        List<Object> saterday = new ArrayList<>();
        List<Object> sunday = new ArrayList<>();

        for (TimeTable t : mTimeTables) {
            String[] parts = t.getStartDate().split("T");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
            try {
                Date date = format.parse(parts[0]);
                Log.d("ContentsLab", "Received date : " + date.toString());
                Log.d("ContentsLab", "day of week : " + dayOfWeek.format(date));

                switch (dayOfWeek.format(date)) {
                    case "Monday" :
                        monday.add(t);
                        break;
                    case "Tuesday" :
                        tuesday.add(t);
                        break;
                    case "Wednesday" :
                        wednesday.add(t);
                        break;
                    case "Thursday" :
                        thursday.add(t);
                        break;
                    case "Friday" :
                        friday.add(t);
                        break;
                    case "Saturday" :
                        saterday.add(t);
                        break;
                    case "Sunday" :
                        sunday.add(t);
                        break;
                    default:
                        Log.d("ContentsLab", "Not found day of week" + dayOfWeek.format(date));
                }

            } catch (ParseException e) {
                Log.e("ContentsLab", "Failed to parse date");
            }
        }

        mTimeTableWeeks.get(0).setChildObjectList(monday);
        mTimeTableWeeks.get(1).setChildObjectList(tuesday);
        mTimeTableWeeks.get(2).setChildObjectList(wednesday);
        mTimeTableWeeks.get(3).setChildObjectList(thursday);
        mTimeTableWeeks.get(4).setChildObjectList(friday);
        mTimeTableWeeks.get(5).setChildObjectList(saterday);
        mTimeTableWeeks.get(6).setChildObjectList(sunday);
    }

}
