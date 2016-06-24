package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;


public class Workflow implements Parcelable {

    @Attribute(name = "resource", required = false)
    String resource;

    @Attribute(name = "uri", required = false)
    String uri;

    @Attribute(name = "id", required = false)
    String id;

    @Attribute(name = "version", required = false)
    String version;

    @Element(name = "title")
    private String title;

    @Element(name = "type")
    private Type type;

    @Element(name = "uploader")
    private Uploader uploader;

    @Element(name = "created-at")
    private String createdAt;

    @Element(name = "svg")
    private String svgUri;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Uploader getUploader() {
        return uploader;
    }

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSvgUri() {
        return svgUri;
    }

    public void setSvgUri(String svgUri) {
        this.svgUri = svgUri;
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
        dest.writeString(this.version);
        dest.writeString(this.title);
        dest.writeParcelable(this.type, flags);
        dest.writeParcelable(this.uploader, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.svgUri);
    }

    public Workflow() {
    }

    protected Workflow(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.version = in.readString();
        this.title = in.readString();
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.uploader = in.readParcelable(Uploader.class.getClassLoader());
        this.createdAt = in.readString();
        this.svgUri = in.readString();
    }

    public static final Creator<Workflow> CREATOR = new Creator<Workflow>() {
        @Override
        public Workflow createFromParcel(Parcel source) {
            return new Workflow(source);
        }

        @Override
        public Workflow[] newArray(int size) {
            return new Workflow[size];
        }
    };
}
