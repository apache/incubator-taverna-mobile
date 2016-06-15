package org.apache.taverna.mobile.data.model;

import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Root(name = "workflows")
public class Workflows implements Parcelable {

    @ElementList(name = "workflow", inline = true, required = false)
    List<Workflow> WorkflowList;

    public List<Workflow> getWorkflowList() {
        return WorkflowList;
    }

    public void setWorkflowList(List<Workflow> workflowList) {
        WorkflowList = workflowList;
    }

    public Workflows() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.WorkflowList);
    }

    protected Workflows(Parcel in) {
        this.WorkflowList = new ArrayList<Workflow>();
        in.readList(this.WorkflowList, Workflow.class.getClassLoader());
    }

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
}
