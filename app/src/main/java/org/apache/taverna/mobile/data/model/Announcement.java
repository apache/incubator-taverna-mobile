package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Sagar
 */
@Root(name = "announcement")
public class Announcement {

    @Element(name = "author")
    private String author;

    @Element(name = "title")
    private String title;

    @Element(name = "text")
    private String text;

    @Element(name = "created-at")
    private String date;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


