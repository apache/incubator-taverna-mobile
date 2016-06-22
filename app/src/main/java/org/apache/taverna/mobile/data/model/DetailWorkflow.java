package org.apache.taverna.mobile.data.model;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@Root(name = "workflow")
public class DetailWorkflow implements Parcelable {

    @Attribute(name = "resource", required = false)
    String resource;
    @Attribute(name = "uri", required = false)
    String uri;
    @Attribute(name = "id", required = false)
    String id;
    @Attribute(name = "version", required = false)
    String version;

    @Element(name = "id")
    private String elementId;
    @Element(name = "title")
    private String title;
    @Element(name = "description")
    String description;
    @Element(name = "type")
    private Type type;
    @Element(name = "uploader")
    private Uploader uploader;
    @Element(name = "created-at")
    private String createdAt;
    @Element(name = "preview")
    private String previewUri;
    @Element(name = "svg")
    private String svgUri;
    @Element(name = "license-type")
    private LicenseType licenseType;
    @Element(name = "content-uri")
    String contentUri;
    @Element(name = "content-type")
    String contentType;
    @ElementList(name = "tags", inline = true, required = false)
    List<Tag> tag;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public String getPreviewUri() {
        return previewUri;
    }

    public void setPreviewUri(String previewUri) {
        this.previewUri = previewUri;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getSvgUri() {
        return svgUri;
    }

    public void setSvgUri(String svgUri) {
        this.svgUri = svgUri;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Uploader getUploader() {
        return uploader;
    }

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        dest.writeString(this.version);
        dest.writeString(this.elementId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(this.type, flags);
        dest.writeParcelable(this.uploader, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.previewUri);
        dest.writeString(this.svgUri);
        dest.writeParcelable(this.licenseType, flags);
        dest.writeString(this.contentUri);
        dest.writeString(this.contentType);
        dest.writeTypedList(this.tag);
    }

    public DetailWorkflow() {
    }

    protected DetailWorkflow(Parcel in) {
        this.resource = in.readString();
        this.uri = in.readString();
        this.id = in.readString();
        this.version = in.readString();
        this.elementId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.uploader = in.readParcelable(Uploader.class.getClassLoader());
        this.createdAt = in.readString();
        this.previewUri = in.readString();
        this.svgUri = in.readString();
        this.licenseType = in.readParcelable(LicenseType.class.getClassLoader());
        this.contentUri = in.readString();
        this.contentType = in.readString();
        this.tag = in.createTypedArrayList(Tag.CREATOR);
    }

    public static final Parcelable.Creator<DetailWorkflow> CREATOR =
            new Parcelable.Creator<DetailWorkflow>() {
                @Override
                public DetailWorkflow createFromParcel(Parcel source) {
                    return new DetailWorkflow(source);
                }

                @Override
                public DetailWorkflow[] newArray(int size) {
                    return new DetailWorkflow[size];
                }
            };
}
