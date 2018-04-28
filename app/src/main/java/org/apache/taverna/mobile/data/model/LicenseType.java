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

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.apache.taverna.mobile.data.local.TavernaBaseModel;
import org.apache.taverna.mobile.data.local.TavernaDatabase;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import android.os.Parcel;
import android.os.Parcelable;

@Table(database = TavernaDatabase.class)
@ModelContainer
public class LicenseType extends TavernaBaseModel implements Parcelable {

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
    @Text(required = false)
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

    public static final Parcelable.Creator<LicenseType> CREATOR =
            new Parcelable.Creator<LicenseType>() {
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
