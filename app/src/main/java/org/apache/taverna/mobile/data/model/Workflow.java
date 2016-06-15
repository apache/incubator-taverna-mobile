package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

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
    @Text
    String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        dest.writeString(this.content);
    }

    public Workflow() {
    }

    protected Workflow(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.version = in.readString();
        this.content = in.readString();
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
