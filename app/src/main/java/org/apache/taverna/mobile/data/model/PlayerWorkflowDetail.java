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