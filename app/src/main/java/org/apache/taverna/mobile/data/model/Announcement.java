package org.apache.taverna.mobile.data.model;

public class Announcement {
    private String id;

    private String content;

    private String resource;

    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", content = " + content + ", resource = " + resource + ", uri = " + uri + "]";
    }
}