package nl.rug.www.summerschool;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

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

    public static ContentsLab get(Context context) {
        if (sContentsLab == null) {
            sContentsLab = new ContentsLab(context);
        }
        return sContentsLab;
    }

    private ContentsLab(Context context) {
        mLogInData = new ArrayList<>();
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

    public ArrayList<String> getLogInData() {
        return mLogInData;
    }

    public void setLogInData(ArrayList<String> logInData) {
        mLogInData = (ArrayList<String>)logInData.clone();
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

    public List<TimeTable> getTimeTables() { return mTimeTables; }

    public TimeTable getTimeTable(String id) {
        for (TimeTable c : mTimeTables) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<TimeTableWeek> getTimeTableWeeks() { return mTimeTableWeeks; }

    public TimeTableWeek getTimeTableWeek(String dayOfWeek) {
        for (TimeTableWeek c : mTimeTableWeeks) {
            if (c.getDayOfWeek().equals(dayOfWeek)) return c;
        }
        return null;
    }

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

    public void createTimeTableWeek() {
        List<Object> monday = new ArrayList<>();
        List<Object> tuesday = new ArrayList<>();
        List<Object> wednesday = new ArrayList<>();
        List<Object> thursday = new ArrayList<>();
        List<Object> friday = new ArrayList<>();
        List<Object> saterday = new ArrayList<>();
        List<Object> sunday = new ArrayList<>();

        for (TimeTable t : mTimeTables) {
            String[] parts = t.getStartDate().split("T");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
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
