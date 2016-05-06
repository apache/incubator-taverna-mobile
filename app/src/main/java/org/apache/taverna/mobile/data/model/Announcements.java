package org.apache.taverna.mobile.data.model;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "announcements")
public class Announcements {

    @ElementList(name = "announcement", inline = true, required = false)
    List<Announcement> announcement;




    public List<Announcement> getAnnouncement() { return this.announcement; }
    public void setAnnouncement(List<Announcement> _value) { this.announcement = _value; }



    public static class Announcement {

        @Attribute(name="resource", required = false)
        String resource;


        @Attribute(name="uri", required = false)
        String uri;


        @Attribute(name="id", required = false)
        String id;

        @Element(name="announcement",required = false)
        String content;

        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }


        public String getResource() { return this.resource; }
        public void setResource(String _value) { this.resource = _value; }


        public String getUri() { return this.uri; }
        public void setUri(String _value) { this.uri = _value; }


        public String getId() { return this.id; }
        public void setId(String _value) { this.id = _value; }


    }
}