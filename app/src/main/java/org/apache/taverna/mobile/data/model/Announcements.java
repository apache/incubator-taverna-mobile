package org.apache.taverna.mobile.data.model;

public class Announcements {
    private Announcement[] announcement;

    public Announcement[] getAnnouncement ()
    {
        return announcement;
    }

    public void setAnnouncement (Announcement[] announcement)
    {
        this.announcement = announcement;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [announcement = "+announcement+"]";
    }
}