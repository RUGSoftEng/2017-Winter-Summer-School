package nl.rug.www.rugsummerschools.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ContentsLab stores data that fetched from the database.
 * Singleton pattern is used so that any class can access to the data.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 13/04/2017
 */

public class ContentsLab {

    /**
     * unique instance of ContentsLab
     */
    private static ContentsLab sContentsLab;

    //    private String mSchoolId;
//    private String mSchoolName;
    private ArrayList<Announcement> mAnnouncements;
    private ArrayList<GeneralInfo> mGeneralInfos;
    private ArrayList<Lecturer> mLecturers;
    private ArrayList<Event> mEvents;
    private ArrayList<ForumThread> mForumThreads;
    private ArrayList<ForumComment> mForumComments;
    private ArrayList<LoginInfo> mSchoolInfos;

    private ContentsLab() {
        mAnnouncements = new ArrayList<>();
        mGeneralInfos = new ArrayList<>();
        mLecturers = new ArrayList<>();
        mForumThreads = new ArrayList<>();
        mSchoolInfos = new ArrayList<>();
        mEvents = new ArrayList<>();
    }

    public static ContentsLab get() {
        if (sContentsLab == null) {
            sContentsLab = new ContentsLab();
        }
        return sContentsLab;
    }

    public LoginInfo getSchoolInfo() {
        return mSchoolInfos.get(0);
    }

    public void updateSchoolInfos(List<LoginInfo> schoolInfos) {
        mSchoolInfos = (ArrayList<LoginInfo>) schoolInfos;
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

    public List<Announcement> getAnnouncements() {
        return mAnnouncements;
    }

    public Announcement getAnnouncement(String id) {
        for (Announcement c : mAnnouncements) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<Lecturer> getLecturers() {
        return mLecturers;
    }

    public Lecturer getLecturer(String id) {
        for (Lecturer c : mLecturers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<Event> getEvents() {
        return mEvents;
    }

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
        mForumComments = (ArrayList<ForumComment>) forumComments;
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
