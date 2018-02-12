package nl.rug.www.rugsummerschools.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ContentsLabTest {

    private ContentsLab mContentsLab;

    private String mSchoolId;
    private ArrayList<Announcement> mAnnouncements;
    private ArrayList<GeneralInfo> mGeneralInfos;
    private ArrayList<Lecturer> mLecturers;
    private ArrayList<Event> mEvents;
    private Date mStartDate;
    private Date mEndDate;
    private ArrayList<ForumThread> mForumThreads;
    private ArrayList<ForumComment> mForumComments;
    private ArrayList<String> mLogInCodes;

    @Before
    public void setUp() throws Exception {
        mContentsLab = ContentsLab.get();
        mAnnouncements = new ArrayList<>();
        Announcement[] announcements = new Announcement[] {
                new Announcement(),
                new Announcement(),
                new Announcement()
        };
        for (int i = 0; i < 3; ++i) {
            announcements[i].setId("identificationNo." + i);
            announcements[i].setTitle("title" + i);
            announcements[i].setDescription("Test-driven development (TDD) is a software development process that relies on the repetition of a very short development cycle: Requirements are turned into very specific test cases, then the software is improved to pass the new tests, only. This is opposed to software development that allows software to be added that is not proven to meet requirements." + i);
            announcements[i].setPoster("author" + i);
            announcements[i].setDate("2018-02-08T15:5" + i +":25.346Z");
            mAnnouncements.add(announcements[i]);
        }
        mContentsLab.updateAnnouncements(mAnnouncements);

        mGeneralInfos = new ArrayList<>();
        GeneralInfo[] generalInfos = new GeneralInfo[] {
                new GeneralInfo(),
                new GeneralInfo(),
                new GeneralInfo()
        };
        for (int i = 0; i < 3; ++i) {
            generalInfos[i].setId("5a7c73a1ab30fc76a07497d3." + i);
            generalInfos[i].setTitle("title00" + i);
            generalInfos[i].setDescription("The static architecture-conformance techniques that we compare are dependency-structure matrices (DSMs),4 source code query languages (SCQLs),5 and reflexion models (RMs).6 We chose these particular techniques because they're representative of the spectrum of available solutions for static architecture conformance, and they're supported by mature and industrial-strength tools that you can apply to systems written in Java. We describe other existing techniques for architecture-conformance analysis in the “Related Work on Architecture Conformance” sidebar." + i);
            generalInfos[i].setCategory("Info" + i);
            mGeneralInfos.add(generalInfos[i]);
        }
        mContentsLab.updateGeneralInfos(mGeneralInfos);

        mLecturers = new ArrayList<>();
        Lecturer[] lecturers = new Lecturer[] {
                new Lecturer(),
                new Lecturer(),
                new Lecturer()
        };
        for (int i = 0; i < 3; ++i) {
            lecturers[i].setId("idNr." + i);
            lecturers[i].setTitle("Alan Turing" + i);
            lecturers[i].setDescription("Alan Mathison Turing OBE FRS (/ˈtjʊərɪŋ/; 23 June 1912 – 7 June 1954) was an English computer scientist, mathematician, logician, cryptanalyst, philosopher, and theoretical biologist." + i);
            lecturers[i].setImgurl("http://www.gogle.com" + i);
            lecturers[i].setWebsite("https://www.alanturing.com/" + i + "/FiniteAutomaton");
            mLecturers.add(lecturers[i]);
        }
        mContentsLab.updateLecturers(mLecturers);

        mEvents = new ArrayList<>();
        Event[] events = new Event[] {
                new Event(),
                new Event(),
                new Event()
        };
        for (int i = 0; i < 3; ++i) {
            events[i].setId("eventNr." + i);
            events[i].setTitle("new event " + i);
            events[i].setSchool("summer school" + i);
            events[i].setLocation("campus" + i);
            events[i].setDescription("This is test description for event" + i);
            mStartDate = new Date();
            mStartDate.setTime(151827902 + i);
            events[i].setStartDate(mStartDate);
            mEndDate = new Date();
            mEndDate.setTime(351827902 + i);
            events[i].setEndDate(mEndDate);
            mEvents.add(events[i]);
        }
        mContentsLab.updateEvents(mEvents);

        mForumComments = new ArrayList<>();
        ForumComment[] comments = new ForumComment[] {
                new ForumComment(),
                new ForumComment(),
                new ForumComment()
        };
        List<String> commentsIdList = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            commentsIdList.add("comments nr." + i);
            comments[i].setId("comments nr." + i);
            comments[i].setDescription("this is comment description " + i);
            comments[i].setPosterId("id" + i + "@google.com");
            comments[i].setPoster("Comment author Nr. " + i);
            comments[i].setImgUrl("https://graph.facebook.com/" + i + "/picture");
            comments[i].setDate("2028-12-28T12:1" + i +":25.346Z");
            comments[i].setTitle("Title Nr." + i);
            mForumComments.add(comments[i]);
        }
        mContentsLab.updateForumComments(mForumComments);

        mForumThreads = new ArrayList<>();
        ForumThread[] threads = new ForumThread[] {
                new ForumThread(),
                new ForumThread(),
                new ForumThread()
        };
        for (int i = 0; i < 3; ++i) {
            threads[i].setId("thread nr." + i);
            threads[i].setTitle("Thread title " + i);
            threads[i].setDescription("This is thread description " + i);
            threads[i].setPosterId("poster id " + i);
            threads[i].setPoster("Poster" + i);
            threads[i].setImgUrl("http://imgurl.com/" + i);
            threads[i].setDate("20" + i +"8-12-28T12:11:25.346Z");
            threads[i].setForumComments(commentsIdList);
            mForumThreads.add(threads[i]);
        }
        mContentsLab.updateForumThreads(mForumThreads);
    }

    @After
    public void tearDown() throws Exception {
        mContentsLab = null;
    }

    @Test
    public void get() {
        assertThat(mContentsLab, is(ContentsLab.get()));
    }

    @Test
    public void testAnnouncements() {
        assertThat(mAnnouncements, is(mContentsLab.getAnnouncements()));

        for (int i = 0; i < 3; ++i) {
            Announcement tmp = mAnnouncements.get(i);
            Announcement test = mContentsLab.getAnnouncement(tmp.getId());
            assertThat(tmp, is(test));
            assertThat("identificationNo." + i, is(test.getId()));
            assertThat("title" + i, is(test.getTitle()));
            assertThat("Test-driven development (TDD) is a software development process that relies on the repetition of a very short development cycle: Requirements are turned into very specific test cases, then the software is improved to pass the new tests, only. This is opposed to software development that allows software to be added that is not proven to meet requirements." + i, is(test.getDescription()));
            assertThat("author" + i, is(test.getPoster()));
            assertThat("2018-02-08T15:5" + i +":25.346Z", is(test.getDate()));
            assertThat("A", is(test.getInitial()));
        }

        List<Announcement> newAnnouncements = new ArrayList<>();
        newAnnouncements.add(mock(Announcement.class));
        newAnnouncements.add(mock(Announcement.class));
        newAnnouncements.add(mock(Announcement.class));
        mContentsLab.updateAnnouncements(newAnnouncements);

        assertThat(newAnnouncements, is(mContentsLab.getAnnouncements()));
    }

    @Test
    public void testGeneralInfos() {
        assertThat(mGeneralInfos, is(mContentsLab.getGeneralInfos()));
        for (int i = 0; i < 3; ++i) {
            GeneralInfo tmp = mGeneralInfos.get(i);
            GeneralInfo test = mContentsLab.getGeneralInfo(tmp.getId());
            assertThat(tmp, is(test));

            assertThat("5a7c73a1ab30fc76a07497d3." + i, is(test.getId()));
            assertThat("title00" + i, is(test.getTitle()));
            assertThat("The static architecture-conformance techniques that we compare are dependency-structure matrices (DSMs),4 source code query languages (SCQLs),5 and reflexion models (RMs).6 We chose these particular techniques because they're representative of the spectrum of available solutions for static architecture conformance, and they're supported by mature and industrial-strength tools that you can apply to systems written in Java. We describe other existing techniques for architecture-conformance analysis in the “Related Work on Architecture Conformance” sidebar." + i, is(test.getDescription()));
            assertThat("Info" + i, is(test.getCategory()));
        }

        List<GeneralInfo> newGeneralInfos = new ArrayList<>();
        newGeneralInfos.add(mock(GeneralInfo.class));
        newGeneralInfos.add(mock(GeneralInfo.class));
        newGeneralInfos.add(mock(GeneralInfo.class));
        mContentsLab.updateGeneralInfos(newGeneralInfos);

        assertThat(newGeneralInfos, is(mContentsLab.getGeneralInfos()));
    }

    @Test
    public void testLecturer() {
        assertThat(mLecturers, is(mContentsLab.getLecturers()));
        for (int i = 0; i < 3; ++i) {
            Lecturer tmp = mLecturers.get(i);
            Lecturer test = mContentsLab.getLecturer(tmp.getId());

            assertThat("idNr." + i, is(test.getId()));
            assertThat("Alan Turing" + i, is(test.getTitle()));
            assertThat("Alan Mathison Turing OBE FRS (/ˈtjʊərɪŋ/; 23 June 1912 – 7 June 1954) was an English computer scientist, mathematician, logician, cryptanalyst, philosopher, and theoretical biologist." + i, is(test.getDescription()));
            assertThat("http://www.gogle.com" + i, is(test.getImgurl()));
            assertThat("https://www.alanturing.com/" + i + "/FiniteAutomaton", is(test.getWebsite()));
        }

        List<Lecturer> newLecturers = new ArrayList<>();
        newLecturers.add(mock(Lecturer.class));
        newLecturers.add(mock(Lecturer.class));
        newLecturers.add(mock(Lecturer.class));
        mContentsLab.updateLecturers(newLecturers);

        assertThat(newLecturers, is(mContentsLab.getLecturers()));
    }

    @Test
    public void testEvents() {
        assertThat(mEvents, is(mContentsLab.getEvents()));
        for (int i = 0; i < 3; ++i) {
            Event tmp = mEvents.get(i);
            Event test = mContentsLab.getEvent(tmp.getId());

            assertThat("eventNr." + i, is(test.getId()));
            assertThat("new event " + i, is(test.getTitle()));
            assertThat("summer school" + i, is(test.getSchool()));
            assertThat("campus" + i, is(test.getLocation()));
            assertThat("This is test description for event" + i, is(test.getDescription()));
            assertThat((long)151827902 + i, is(test.getStartDate().getTime()));
            assertThat((long)351827902 + i, is(test.getEndDate().getTime()));
        }

        List<Event> newEvents = new ArrayList<>();
        newEvents.add(mock(Event.class));
        newEvents.add(mock(Event.class));
        newEvents.add(mock(Event.class));
        mContentsLab.updateEvents(newEvents);

        assertThat(newEvents, is(mContentsLab.getEvents()));
    }

    @Test
    public void testForumComments() {
        ArrayList<ForumComment> list = mContentsLab.getForumComments();
        assertThat(mForumComments, is(list));
        for (int i = 0; i < 3; ++i) {
            ForumComment test = list.get(i);
            assertThat("comments nr." + i, is(test.getId()));
            assertThat("this is comment description " + i, is(test.getDescription()));
            assertThat("id" + i + "@google.com", is(test.getPosterId()));
            assertThat("Comment author Nr. " + i, is(test.getPoster()));
            assertThat("https://graph.facebook.com/" + i + "/picture", is(test.getImgUrl()));
            assertThat("2028-12-28T12:1" + i +":25.346Z", is(test.getDate()));
            assertThat("Title Nr." + i, is(test.getTitle()));
        }

        list = new ArrayList<>();
        list.add(mock(ForumComment.class));
        list.add(mock(ForumComment.class));
        list.add(mock(ForumComment.class));
        mContentsLab.updateForumComments(list);
        assertThat(list, is(mContentsLab.getForumComments()));
    }

    @Test
    public void testForumThreads() {
        for (int i = 0; i < 3; ++i) {
            ForumThread test = mContentsLab.getForumThread("thread nr." + i);

            assertThat("thread nr." + i, is(test.getId()));
            assertThat("Thread title " + i, is(test.getTitle()));
            assertThat("This is thread description " + i, is(test.getDescription()));
            assertThat("poster id " + i, is(test.getPosterId()));
            assertThat("Poster" + i, is(test.getPoster()));
            assertThat("http://imgurl.com/" + i, is(test.getImgUrl()));
            assertThat("20" + i +"8-12-28T12:11:25.346Z", is(test.getDate()));
            List<String> commentIds = test.getForumComments();
            for (int j = 0; j < 3; ++j) {
                assertThat("comments nr." + j, is(commentIds.get(j)));
            }
        }
    }
}