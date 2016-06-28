/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.taverna.mobile.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;


@Root(name = "announcement")
public class DetailAnnouncement implements Parcelable {

    @Attribute(name = "resource", required = false)
    String resource;

    @Attribute(name = "uri", required = false)
    String uri;

    @Attribute(name = "id", required = false)
    String id;

    @Element(name = "author")
    private Author author;

    @Element(name = "title")
    private String title;

    @Element(name = "text")
    private String text;

    @Element(name = "created-at")
    private String date;

    @Element(name = "id")
    private String idElement;

    public String getIdElement() {
        return idElement;
    }

    public void setIdElement(String idElement) {
        this.idElement = idElement;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String _value) {
        this.resource = _value;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String _value) {
        this.uri = _value;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String _value) {
        this.id = _value;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author1) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resource);
        dest.writeString(this.uri);
        dest.writeString(this.id);
        dest.writeParcelable(this.author, flags);
        dest.writeString(this.title);
        dest.writeString(this.text);
        dest.writeString(this.date);
        dest.writeString(this.idElement);
    }

    public DetailAnnouncement() {
    }

    protected DetailAnnouncement(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.author = in.readParcelable(Author.class.getClassLoader());
        this.title = in.readString();
        this.text = in.readString();
        this.date = in.readString();
        this.idElement = in.readString();
    }

    public static final Creator<DetailAnnouncement> CREATOR = new Creator<DetailAnnouncement>() {
        @Override
        public DetailAnnouncement createFromParcel(Parcel source) {
            return new DetailAnnouncement(source);
        }

        @Override
        public DetailAnnouncement[] newArray(int size) {
            return new DetailAnnouncement[size];
        }
    };
}


