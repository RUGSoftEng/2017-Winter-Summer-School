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
    private ArrayList<EventsPerDay> mEventsPerDays;
    private ArrayList<Lecturer> mLecturers;
    private ArrayList<ForumThread> mForumThreads;
    private ArrayList<String> mLogInData;
    private ArrayList<String> mFakeUsers;

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
        mForumThreads = new ArrayList<>();
        mFakeUsers = new ArrayList<>();
        createFakeUsers();
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

    public List<ForumThread> getForumThreads() {
        return mForumThreads;
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

    public void updateForumThreads(List<ForumThread> forumThreads) {
        mForumThreads = (ArrayList<ForumThread>) forumThreads;
    }

    public ArrayList<String> getmLogInData() {
        return mLogInData;
    }

    public void setmLogInData(ArrayList<String> mLogInData) {
        this.mLogInData = mLogInData;
    }

    public boolean userExist(String UID) {
        for(int i = 0; i < mFakeUsers.size(); i++){
            if(UID.equals(mFakeUsers.get(i))) return true;
        }
        return false;
    }

    public void addFakeData(String data){
        mLogInData.add(data);
    }

    public void addFakeUsers(String UID) {
        mFakeUsers.add(UID);
    }

    private void createFakeUsers(){
        mFakeUsers.add("lPgWPazXwVSq5XYl3FiK6GAmaNE3");
    }

    public void printUsers(){
        for(int i = 0; i < mFakeUsers.size(); i++){
            Log.i("USER STRING", mFakeUsers.get(i));
        }
    }
}
