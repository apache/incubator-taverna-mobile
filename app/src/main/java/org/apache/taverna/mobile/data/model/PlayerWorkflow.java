package org.apache.taverna.mobile.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerWorkflow implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
    }

    public PlayerWorkflow() {
    }

    protected PlayerWorkflow(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlayerWorkflow> CREATOR = new Parcelable
            .Creator<PlayerWorkflow>() {
        @Override
        public PlayerWorkflow createFromParcel(Parcel source) {
            return new PlayerWorkflow(source);
        }

        @Override
        public PlayerWorkflow[] newArray(int size) {
            return new PlayerWorkflow[size];
        }
    };
}