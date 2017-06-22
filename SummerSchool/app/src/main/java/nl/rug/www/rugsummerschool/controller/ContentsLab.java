package nl.rug.www.rugsummerschool.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.rugsummerschool.model.Announcement;
import nl.rug.www.rugsummerschool.model.EventsPerDay;
import nl.rug.www.rugsummerschool.model.ForumThread;
import nl.rug.www.rugsummerschool.model.GeneralInfo;
import nl.rug.www.rugsummerschool.model.Lecturer;

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
    private ArrayList<EventsPerDay> mPreviousWeekEvents;
    private ArrayList<EventsPerDay> mCurrentWeekEvents;
    private ArrayList<EventsPerDay> mNextWeekEvents;
    private ArrayList<Lecturer> mLecturers;
    private ArrayList<ForumThread> mForumThreads;
    private ArrayList<String> mLogInData;
    private ArrayList<String> mLogInCodes;

    public static ContentsLab get() {
        if (sContentsLab == null) {
            sContentsLab = new ContentsLab();
        }
        return sContentsLab;
    }

    private ContentsLab() {
        mAnnouncements = new ArrayList<>();
        mGeneralInfos = new ArrayList<>();
        mPreviousWeekEvents = new ArrayList<>();
        mCurrentWeekEvents = new ArrayList<>();
        mNextWeekEvents = new ArrayList<>();
        mLecturers = new ArrayList<>();
        mForumThreads = new ArrayList<>();
        mLogInCodes = new ArrayList<>();
    }

    public List<GeneralInfo> getGeneralInfos() {
        return mGeneralInfos;
    }

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

    public List<EventsPerDay> getPreviousWeekEvents() { return mPreviousWeekEvents; }

    public List<EventsPerDay> getCurrentWeekEvents() { return mCurrentWeekEvents; }

    public List<EventsPerDay> getNextWeekEvents() { return mNextWeekEvents; }

    public List<Lecturer> getLecturers() { return mLecturers; }

    public Lecturer getLecturer(String id) {
        for (Lecturer c : mLecturers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public boolean checkLogInCode(String logInCode) {
        for(int i = 0; i < mLogInCodes.size(); ++i) {
            if (mLogInCodes.get(i).equals(logInCode)) return true;
        }

        return false;
    }

    public void updateAnnouncements(List<Announcement> announcements) {
        mAnnouncements = (ArrayList<Announcement>) announcements;
    }

    public void updateGeneralInfos(List<GeneralInfo> generalInfos) {
        mGeneralInfos = (ArrayList<GeneralInfo>) generalInfos;
    }

    public void updateLecturers(List<Lecturer> lecturers) {
        mLecturers = (ArrayList<Lecturer>) lecturers;
    }

    public void updatePreviousWeekTimeTable(List<EventsPerDay> eventsPerDays) {
        mPreviousWeekEvents = (ArrayList<EventsPerDay>) eventsPerDays;
    }

    public void updateTimeTableWeeks(List<EventsPerDay> eventsPerDays) {
        mCurrentWeekEvents = (ArrayList<EventsPerDay>) eventsPerDays;
    }

    public void updateNextWeekTimeTable(List<EventsPerDay> eventsPerDays) {
        mNextWeekEvents = (ArrayList<EventsPerDay>) eventsPerDays;
    }

    public void updateForumThreads(List<ForumThread> forumThreads) {
        mForumThreads = (ArrayList<ForumThread>) forumThreads;
    }

    public void updateLogInCodes(List<String> logInCodes) {
        mLogInCodes = (ArrayList<String>) logInCodes;
    }

    public ArrayList<String> getmLogInData() {
        return mLogInData;
    }

    public void setmLogInData(ArrayList<String> mLogInData) {
        this.mLogInData = mLogInData;
    }
}
