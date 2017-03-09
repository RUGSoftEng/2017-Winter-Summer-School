package nl.rug.www.summerschool;

/**
 * Created by jk on 09/03/17.
 */

public class Announcement {

    private String title;
    private String contents;
    private String date;
    private String author;

    public Announcement(String title, String contents, String date, String author) {
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }
}
