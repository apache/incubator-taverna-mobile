package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Sagar
 */
@Root(name = "announcement")
public class Announcement {

    @Element(name = "author")
    private author author;

    @Element(name = "title")
    private String title;

    @Element(name = "text")
    private String text;

    @Element(name = "created-at")
    private String date;

    @Attribute(name="resource", required = false)
    String resource;


    @Attribute(name="uri", required = false)
    String uri;


    @Attribute(name="id", required = false)
    String id;

    @Element(name = "id")
    private String idElement;

    public String getIdElement() {
        return idElement;
    }

    public void setIdElement(String idElement) {
        this.idElement = idElement;
    }

    public String getResource() { return this.resource; }
    public void setResource(String _value) { this.resource = _value; }


    public String getUri() { return this.uri; }
    public void setUri(String _value) { this.uri = _value; }


    public String getId() { return this.id; }
    public void setId(String _value) { this.id = _value; }


    public author getAuthor() {
        return author;
    }

    public void setAuthor(author author1) {
        this.author = author1;
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

    public static class author {

        @Attribute(name="resource", required = false)
        String resource;


        @Attribute(name="uri", required = false)
        String uri;


        @Attribute(name="id", required = false)
        String id;

        @Text
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


