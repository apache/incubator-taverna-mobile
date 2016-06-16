package org.apache.taverna.mobile.data.model;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import android.os.Parcel;
import android.os.Parcelable;

public class LicenseType implements Parcelable {

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

    public LicenseType() {
    }

    protected LicenseType(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<LicenseType> CREATOR = new Parcelable.Creator<LicenseType>() {
        @Override
        public LicenseType createFromParcel(Parcel source) {
            return new LicenseType(source);
        }

        @Override
        public LicenseType[] newArray(int size) {
            return new LicenseType[size];
        }
    };
}
