package nl.rug.www.rugsummerschools.controller;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.Event;
import nl.rug.www.rugsummerschools.model.EventsPerDay;
import nl.rug.www.rugsummerschools.model.ForumComment;
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.model.GeneralInfo;
import nl.rug.www.rugsummerschools.model.Lecturer;

/**
 * ContentsLab stores data that fetched from the database.
 * Singleton pattern is used so that any class can access to the data.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public class ContentsLab {

    /** unique instance of ContentsLab */
    private static ContentsLab sContentsLab;

    private String mSchoolId;
    private ArrayList<Announcement> mAnnouncements;
    private ArrayList<GeneralInfo> mGeneralInfos;
    private ArrayList<Lecturer> mLecturers;
    private ArrayList<Event> mEvents;
    private ArrayList<ForumThread> mForumThreads;
    private ArrayList<ForumComment> mForumComments;
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
        mLecturers = new ArrayList<>();
        mForumThreads = new ArrayList<>();
        mLogInCodes = new ArrayList<>();
        mEvents = new ArrayList<>();
    }

    public String getSchoolId() { return mSchoolId; }

    public void setSchoolId(String id) { mSchoolId = id; }

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

    public List<Lecturer> getLecturers() { return mLecturers; }

    public Lecturer getLecturer(String id) {
        for (Lecturer c : mLecturers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<Event> getEvents() { return mEvents; }

    public Event getEvent(String id) {
        for (Event e : mEvents) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    public ForumThread getForumThread(String id) {
        for (ForumThread f : mForumThreads) {
            if (f.getId().equals(id)) return f;
        }
        return null;
    }

    public ArrayList<ForumComment> getForumComments() {
        return mForumComments;
    }

    public void updateForumComments(List<ForumComment> forumComments) {
        mForumComments = (ArrayList<ForumComment>)forumComments;
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

    public void updateEvents(List<Event> events) {
        mEvents = (ArrayList<Event>) events;
    }

    public void updateForumThreads(List<ForumThread> forumThreads) {
        mForumThreads = (ArrayList<ForumThread>) forumThreads;
    }
}
