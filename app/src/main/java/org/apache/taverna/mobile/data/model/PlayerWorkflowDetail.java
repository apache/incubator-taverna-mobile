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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerWorkflowDetail implements Parcelable {

    @SerializedName("run")
    @Expose
    private Run run;

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.run, flags);
    }

    public PlayerWorkflowDetail() {
    }

    protected PlayerWorkflowDetail(Parcel in) {
        this.run = in.readParcelable(Run.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlayerWorkflowDetail> CREATOR = new Parcelable
            .Creator<PlayerWorkflowDetail>() {
        @Override
        public PlayerWorkflowDetail createFromParcel(Parcel source) {
            return new PlayerWorkflowDetail(source);
        }

        @Override
        public PlayerWorkflowDetail[] newArray(int size) {
            return new PlayerWorkflowDetail[size];
        }
    };
}