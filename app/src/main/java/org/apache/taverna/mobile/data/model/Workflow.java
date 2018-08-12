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

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.apache.taverna.mobile.data.local.TavernaBaseModel;
import org.apache.taverna.mobile.data.local.TavernaDatabase;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Table(database = TavernaDatabase.class)
@ModelContainer
@Root(name = "workflow")
public class Workflow extends TavernaBaseModel implements Parcelable {

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
    @Column
    @Attribute(name = "resource", required = false)
    String resource;
    @Column
    @Attribute(name = "uri", required = false)
    String uri;
    @PrimaryKey
    @Attribute(name = "id", required = false)
    String id;
    @Column
    @Attribute(name = "version", required = false)
    String version;
    @Column
    @Element(name = "id", required = false)
    String elementId;
    @Column
    @Element(name = "title", required = false)
    String title;
    @Column
    @Element(name = "description", required = false)
    String description;
    @Column
    @ForeignKey(saveForeignKeyModel = true)
    @Element(name = "type", required = false)
    Type type;
    @Column
    @ForeignKey(saveForeignKeyModel = true)
    @Element(name = "uploader", required = false)
    Uploader uploader;
    @Column
    @Element(name = "created-at", required = false)
    String createdAt;
    @Column
    @Element(name = "updated-at", required = false)
    String updatedAt;
    @Column
    @Element(name = "preview", required = false)
    String previewUri;
    @Column
    @Element(name = "svg", required = false)
    String svgUri;
    @Column
    @ForeignKey(saveForeignKeyModel = true)
    @Element(name = "license-type", required = false)
    LicenseType licenseType;
    @Column
    @Element(name = "content-uri", required = false)
    String contentUri;
    @Column
    @Element(name = "content-type", required = false)
    String contentType;
    @ElementList(name = "tags", required = false)
    List<Tag> tag;
    @Column(defaultValue = "0")
    Boolean favourite;

    public Workflow() {
    }

    protected Workflow(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.version = in.readString();
        this.elementId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.uploader = in.readParcelable(Uploader.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.previewUri = in.readString();
        this.svgUri = in.readString();
        this.licenseType = in.readParcelable(LicenseType.class.getClassLoader());
        this.contentUri = in.readString();
        this.contentType = in.readString();
        this.tag = in.createTypedArrayList(Tag.CREATOR);
        this.favourite = in.readByte() != 0;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

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

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public String getPreviewUri() {
        return previewUri;
    }

    public void setPreviewUri(String previewUri) {
        this.previewUri = previewUri;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getSvgUri() {
        return svgUri;
    }

    public void setSvgUri(String svgUri) {
        this.svgUri = svgUri;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
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
        dest.writeString(this.elementId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(this.type, flags);
        dest.writeParcelable(this.uploader, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.previewUri);
        dest.writeString(this.svgUri);
        dest.writeParcelable(this.licenseType, flags);
        dest.writeString(this.contentUri);
        dest.writeString(this.contentType);
        dest.writeTypedList(this.tag);
        dest.writeByte(this.favourite ? (byte) 1 : (byte) 0);
    }
}
