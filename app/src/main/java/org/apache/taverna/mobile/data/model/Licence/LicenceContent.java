package org.apache.taverna.mobile.data.model.licence;

import com.google.gson.annotations.SerializedName;


public class LicenceContent {

    @SerializedName("library_name")
    private String name;

    private String version;

    private String licence;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getLicence() {
        return licence;
    }

    @Override
    public String toString() {
        return name + " " + version + " " + licence;
    }

}
