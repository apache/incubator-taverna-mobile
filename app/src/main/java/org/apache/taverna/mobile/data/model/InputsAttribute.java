package org.apache.taverna.mobile.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputsAttribute implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("value")
    @Expose
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public InputsAttribute() {
    }

    protected InputsAttribute(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<InputsAttribute> CREATOR = new Creator<InputsAttribute>() {
        @Override
        public InputsAttribute createFromParcel(Parcel source) {
            return new InputsAttribute(source);
        }

        @Override
        public InputsAttribute[] newArray(int size) {
            return new InputsAttribute[size];
        }
    };
}