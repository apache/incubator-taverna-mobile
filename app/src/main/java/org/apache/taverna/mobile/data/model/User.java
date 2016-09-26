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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "user")
public class User implements Parcelable {

    @Attribute(name = "resource", required = false)
    private String resource;

    @Attribute(name = "uri", required = false)
    private String uri;

    @Attribute(name = "id", required = false)
    private String id;

    @Element(name = "id", required = false)
    private String elementId;

    @Element(name = "created-at", required = false)
    private String createdAt;

    @Element(name = "name", required = false)
    private String name;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "email", required = false)
    private String email;

    @Element(name = "city", required = false)
    private String city;

    @Element(name = "country", required = false)
    private String country;

    @Element(name = "website", required = false)
    private String website;

    @Element(name = "avatar", required = false)
    private Avatar avatar;

    @Element(name = "workflows", required = false)
    private Workflows workflows;

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

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public User() {
    }

    public Workflows getWorkflows() {
        return workflows;
    }

    public void setWorkflows(Workflows workflows) {
        this.workflows = workflows;
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
        dest.writeString(this.createdAt);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.email);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.website);
        dest.writeParcelable(this.avatar, flags);
        dest.writeParcelable(this.workflows, flags);
    }

    protected User(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.elementId = in.readString();
        this.createdAt = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.email = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        this.website = in.readString();
        this.avatar = in.readParcelable(Avatar.class.getClassLoader());
        this.workflows = in.readParcelable(Workflows.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}