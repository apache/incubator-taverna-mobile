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

import java.util.ArrayList;
import java.util.List;

@Root(name = "workflows")
public class Workflows implements Parcelable {

    public static final Creator<Workflows> CREATOR = new Creator<Workflows>() {
        @Override
        public Workflows createFromParcel(Parcel source) {
            return new Workflows(source);
        }

        @Override
        public Workflows[] newArray(int size) {
            return new Workflows[size];
        }
    };
    @ElementList(name = "workflow", inline = true, required = false)
    List<Workflow> WorkflowList;

    public Workflows() {
    }

    protected Workflows(Parcel in) {
        this.WorkflowList = new ArrayList<Workflow>();
        in.readList(this.WorkflowList, Workflow.class.getClassLoader());
    }

    public List<Workflow> getWorkflowList() {
        return WorkflowList;
    }

    public void setWorkflowList(List<Workflow> workflowList) {
        WorkflowList = workflowList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.WorkflowList);
    }
}
