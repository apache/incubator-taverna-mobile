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

import java.util.ArrayList;
import java.util.List;

public class Run implements Parcelable {

    @SerializedName("workflow_id")
    @Expose
    private Integer workflowId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("inputs_attributes")
    @Expose
    private List<InputsAttribute> inputsAttributes = new ArrayList<InputsAttribute>();

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InputsAttribute> getInputsAttributes() {
        return inputsAttributes;
    }

    public void setInputsAttributes(List<InputsAttribute> inputsAttributes) {
        this.inputsAttributes = inputsAttributes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.workflowId);
        dest.writeString(this.name);
        dest.writeList(this.inputsAttributes);
    }

    public Run() {
    }

    protected Run(Parcel in) {
        this.workflowId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.inputsAttributes = new ArrayList<InputsAttribute>();
        in.readList(this.inputsAttributes, InputsAttribute.class.getClassLoader());
    }

    public static final Parcelable.Creator<Run> CREATOR = new Parcelable.Creator<Run>() {
        @Override
        public Run createFromParcel(Parcel source) {
            return new Run(source);
        }

        @Override
        public Run[] newArray(int size) {
            return new Run[size];
        }
    };
}