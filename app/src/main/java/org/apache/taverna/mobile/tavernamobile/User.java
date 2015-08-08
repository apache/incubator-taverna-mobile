package org.apache.taverna.mobile.tavernamobile;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation

 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).

 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import android.graphics.Bitmap;

import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.simpleframework.xml.Element;

import java.util.List;

/**
 * Created by root on 6/18/15.
 */
public class User {

    private static final long serialVersionUID = 3467195671046297377L;
    @Element(required = false)
    protected String id;
    @Element(name = "created-at", required = false)
    protected String created_at;
    @Element(required = false)
    protected String name;
    @Element(required = false)
    protected String description;
    @Element(required = false)
    protected String email;
    @Element(required = false)
    protected Bitmap avatar;
    @Element(required = false)
    protected String city;
    @Element(required = false)
    protected String country;
    @Element(required = false)
    protected String website;
    protected String details_uri;
    private String avatar_url;
    private String row_id; //identifies the row  to which this user is being loaded in, in the workflow listview
    private WorkflowAdapter.ViewHolder userViewHolder;

    public User(String rid, WorkflowAdapter.ViewHolder vh){
        super();
        row_id = rid;
        this.userViewHolder = vh;
    }

    public WorkflowAdapter.ViewHolder getUserViewHolder() {
        return userViewHolder;
    }

    public String getRow_id() {
        return this.row_id;
    }

    protected List<Workflow> user_workflows; //a list of workflows owned by this user

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDetails_uri() {
        return this.details_uri;
    }

    public void setDetails_uri(String details_uri) {
        this.details_uri = details_uri;
    }

    public String getAvatar_url() {
        return this.avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString(){
        return "This is the user object";
    }
}
