package org.apache.taverna.mobile.data.model;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name = "avatar")
public class Avatar implements Parcelable {

    @Attribute(name = "resource", required = false)
    private String resource;

    @Attribute(name = "uri", required = false)
    private String uri;

    @Attribute(name = "id", required = false)
    private String id;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resource);
        dest.writeString(this.uri);
        dest.writeString(this.id);
    }

    public Avatar() {
    }

    protected Avatar(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<Avatar> CREATOR = new Parcelable.Creator<Avatar>() {
        @Override
        public Avatar createFromParcel(Parcel source) {
            return new Avatar(source);
        }

        @Override
        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };
}
