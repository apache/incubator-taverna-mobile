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

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "announcements")
public class Announcements implements Parcelable {

    public static final Parcelable.Creator<Announcements> CREATOR = new Parcelable
            .Creator<Announcements>() {
        @Override
        public Announcements createFromParcel(Parcel source) {
            return new Announcements(source);
        }

        @Override
        public Announcements[] newArray(int size) {
            return new Announcements[size];
        }
    };
    @ElementList(name = "announcement", inline = true, required = false)
    List<Announcement> announcement;

    public Announcements() {
    }

    protected Announcements(Parcel in) {
        this.announcement = in.createTypedArrayList(Announcement.CREATOR);
    }

    public List<Announcement> getAnnouncement() {
        return this.announcement;
    }

    public void setAnnouncement(List<Announcement> _value) {
        this.announcement = _value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.announcement);
    }
}