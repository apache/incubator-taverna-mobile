package org.apache.taverna.mobile.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by Sagar
 */
public class Author implements Parcelable {

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

        public Author() {
        }

        protected Author(Parcel in) {
                this.resource = in.readString();
                this.uri = in.readString();
                this.id = in.readString();
                this.content = in.readString();
        }

        public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
                @Override
                public Author createFromParcel(Parcel source) {
                        return new Author(source);
                }

                @Override
                public Author[] newArray(int size) {
                        return new Author[size];
                }
        };
}