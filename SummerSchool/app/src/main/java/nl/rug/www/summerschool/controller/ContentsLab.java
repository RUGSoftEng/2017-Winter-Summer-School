package nl.rug.www.summerschool.controller;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.summerschool.model.Announcement;
import nl.rug.www.summerschool.model.GeneralInfo;
import nl.rug.www.summerschool.model.Lecturer;
import nl.rug.www.summerschool.model.EventsPerDay;

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
    private ArrayList<EventsPerDay> mEventsPerDays;
    private ArrayList<Lecturer> mLecturers;

    public static ContentsLab get() {
        if (sContentsLab == null) {
            sContentsLab = new ContentsLab();
        }
        return sContentsLab;
    }

    private ContentsLab() {
        mAnnouncements = new ArrayList<>();
        mGeneralInfos = new ArrayList<>();
        mEventsPerDays = new ArrayList<>();
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

    public List<EventsPerDay> getEventsPerDays() { return mEventsPerDays; }

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

    public void updateLecturers(List<Lecturer> lecturers) {
        mLecturers = (ArrayList<Lecturer>) lecturers;
    }

    public void updateTimeTableWeeks(List<EventsPerDay> eventsPerDays) {
        mEventsPerDays = (ArrayList<EventsPerDay>) eventsPerDays;
    }
}
