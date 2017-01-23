package org.apache.taverna.mobile.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by ian on 16/01/17.
 */

@Root(name="input")
public class Input {
    @Attribute(name = "name")
    private String name;

    @Attribute(name = "depth")
    private String depth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Attribute(name = "href", required = false)
    private String href;
}
