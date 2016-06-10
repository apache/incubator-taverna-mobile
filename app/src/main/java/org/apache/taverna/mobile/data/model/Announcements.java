package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Sagar
 */
@Root(name = "announcements")
public class Announcements implements Parcelable {

    public static final Parcelable.Creator<Announcements> CREATOR = new Parcelable
            .Creator<Announcements>() {
        @Override
        public Announcements createFromParcel(Parcel source) {
            return new Announcements(source);
        }

        @Override
        public Announcements[] newArray(int size) {
            return new Announcements[size];
        }
    };
    @ElementList(name = "announcement", inline = true, required = false)
    List<Announcement> announcement;

    public Announcements() {
    }


    protected Announcements(Parcel in) {
        this.announcement = in.createTypedArrayList(Announcement.CREATOR);
    }

    public List<Announcement> getAnnouncement() {
        return this.announcement;
    }

    public void setAnnouncement(List<Announcement> _value) {
        this.announcement = _value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.announcement);
    }
}