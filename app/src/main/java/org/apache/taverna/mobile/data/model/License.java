package org.apache.taverna.mobile.data.model;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;

public class License implements Parcelable {

    @Attribute(name = "resource", required = false)
    private String resource;

    @Attribute(name = "uri", required = false)
    private String uri;

    @Attribute(name = "id", required = false)
    private String id;

    @Element(name = "id", required = false)
    private String elementId;

    @Element(name = "unique-name", required = false)
    private String uniqueName;

    @Element(name = "title", required = false)
    private String title;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "url", required = false)
    private String url;

    @Element(name = "created-at", required = false)
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resource);
        dest.writeString(this.uri);
        dest.writeString(this.id);
        dest.writeString(this.elementId);
        dest.writeString(this.uniqueName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.createdAt);
    }

    public License() {
    }

    protected License(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.elementId = in.readString();
        this.uniqueName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<License> CREATOR = new Parcelable.Creator<License>() {
        @Override
        public License createFromParcel(Parcel source) {
            return new License(source);
        }

        @Override
        public License[] newArray(int size) {
            return new License[size];
        }
    };
}
