package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name = "uploader")
public class Uploader implements Parcelable {
    @Attribute(name = "resource", required = false)
    String resource;
    @Attribute(name = "uri", required = false)
    String uri;
    @Attribute(name = "id", required = false)
    String id;
    @Text
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        dest.writeString(this.content);
    }

    public Uploader() {
    }

    protected Uploader(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Uploader> CREATOR = new Parcelable.Creator<Uploader>() {
        @Override
        public Uploader createFromParcel(Parcel source) {
            return new Uploader(source);
        }

        @Override
        public Uploader[] newArray(int size) {
            return new Uploader[size];
        }
    };
}
