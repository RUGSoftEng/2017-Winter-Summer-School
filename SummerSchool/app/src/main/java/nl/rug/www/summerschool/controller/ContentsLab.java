package nl.rug.www.summerschool.controller;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.summerschool.model.Announcement;
import nl.rug.www.summerschool.model.ForumComment;
import nl.rug.www.summerschool.model.ForumThread;
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
    private ArrayList<ForumThread> mForumThreads;

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
        createFakeData();
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

    private void createFakeData() {
        ForumThread f1 = new ForumThread();
        f1.setTitle("Cousin Tribulation's Story");
        f1.setDate("2017-05-14");
        f1.setPoster("Louisa May Alcott");
        f1.setDescription("Dear Merrys:--As a subject appropriate to the season, I want to tell you about a New Year's breakfast which I had when I was a little girl. What do you think it was? A slice of dry bread and an apple. This is how it happened, and it is a true story, every word.");
        ForumComment f11 = new ForumComment();
        f11.setPoster("Jeongkyun Oh");
        f11.setDate("2017-05-15");
        f11.setText("It is hella fun");
        ForumComment f12 = new ForumComment();
        f12.setPoster("Nick");
        f12.setDate("2017-05-20");
        f12.setText("No, it is not fun");
        List<ForumComment> forumComments = new ArrayList<>();
        forumComments.add(f11);
        forumComments.add(f12);
        f1.setForumCommentList(forumComments);

        ForumThread f2 = new ForumThread();
        f2.setTitle("The Story of An Hour");
        f2.setDate("2017-05-14");
        f2.setPoster("Kate Chopin");
        f2.setDescription("It was her sister Josephine who told her, in broken sentences; veiled hints that revealed in half concealing. Her husband's friend Richards was there, too, near her. It was he who had been in the newspaper office when intelligence of the railroad disaster was received, with Brently Mallard's name leading the list of \"killed.\" He had only taken the time to assure himself of its truth by a second telegram, and had hastened to forestall any less careful, less tender friend in bearing the sad message.");

        ForumThread f3 = new ForumThread();
        f3.setTitle("The Hanging Stranger");
        f3.setDescription("2017-05-20");
        f3.setPoster("Philip K. Dick");
        f3.setDescription("It was getting dark. The setting sun cast long rays over the scurrying commuters, tired and grim-faced, women loaded down with bundles and packages, students, swarming home from the university, mixing with clerks and businessmen and drab secretaries. He stopped his Packard for a red light and then started it up again. The store had been open without him; he'd arrive just in time to spell the help for dinner, go over the records of the day, maybe even close a couple of sales himself. He drove slowly past the small square of green in the center of the street, the town park. There were no parking places in front of LOYCE TV SALES AND SERVICE. He cursed under his breath and swung the car in a U-turn. Again he passed the little square of green with its lonely drinking fountain and bench and single lamppost.");

        mForumThreads.add(f1);
        mForumThreads.add(f2);
        mForumThreads.add(f3);
    }
}
