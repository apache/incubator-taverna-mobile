package org.apache.taverna.mobile.utils.xmlparsers;
/**
 * Apache Taverna Mobile
 * Copyright 2015 The Apache Software Foundation
 *
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 *
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

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.rule.DefaultRule;

import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;

import android.text.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/23/15.
 */
public class MyExperimentXmlParserRules {

    public static final Workflow mWorkflow = new Workflow();

    public MyExperimentXmlParserRules() {
    }

    public Workflow getWorkflowHere() {
        return mWorkflow;
    }

    //parse a single workflow from myexperiment
    public static final class WorkflowDetailRule extends DefaultRule {

        public WorkflowDetailRule(Type type, String locationPath, String... attributeNames)
                throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {
            switch (index) {
                case 0: //uri
                    mWorkflow.setWorkflowDetailsUrl(value);
                    ((Workflow) userObject).setWorkflowDetailsUrl(value);
                    break;
                case 1: //resource
                    mWorkflow.setWorkflowWebUrl(value);
                    ((Workflow) userObject).setWorkflowWebUrl(value);
                    break;
                case 2: //id
                    mWorkflow.setId(Integer.parseInt(value));
                    ((Workflow) userObject).setId(Integer.parseInt(value));
                    break;
                case 3://version
                    mWorkflow.setWorkflowVersions(value);
                    ((Workflow) userObject).setWorkflowVersions(value);
                    break;
            }
        }
    }

    public static class TitleRule extends DefaultRule {

        public TitleRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowTitle(text);
            ((Workflow) userObject).setWorkflowTitle(text);
        }
    }

    public static class DescriptionRule extends DefaultRule {

        public DescriptionRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {

            mWorkflow.setWorkflowDescription(String.valueOf(Html.fromHtml(text)));
            ((Workflow) userObject).setWorkflowDescription(String.valueOf(Html.fromHtml(text)));
        }
    }

    public static class TypeRule extends DefaultRule {

        public TypeRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {
            switch (index) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;

            }
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowType(text);
            ((Workflow) userObject).setWorkflowType(text);
        }
    }

    public static class UploaderRule extends DefaultRule {

        User muser;

        public UploaderRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
            muser = new User("", null);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {
            switch (index) {
                case 0:
                    muser.setWebsite(value);
                    if ((userObject instanceof User)) {
                        ((User) userObject).setWebsite(value);
                    }
                    break;
                case 1:
                    muser.setDetailsUri(value);
                    if ((userObject instanceof User)) {
                        ((User) userObject).setDetailsUri(value);
                    }
                    break;
                case 2:
                    muser.setId(value);
                    if ((userObject instanceof User)) {
                        ((User) userObject).setId(value);
                    }
                    break;
            }
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            muser.setName(text);
            mWorkflow.setUploader(muser);

            if ((userObject instanceof User)) {
                ((User) userObject).setName(text);
            } else {
                ((Workflow) userObject).setUploader(muser);
            }
        }
    }

    //rule used to parse author from main page
    public static class AuthorRule extends DefaultRule {

        public AuthorRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {
            switch (index) {
                case 0:
                    ((User) userObject).setAvatarUrl(value);
                    break;
                case 1:
                    ((User) userObject).setDetailsUri(value);
                    break;
                case 2:
                    ((User) userObject).setId(value);
                    break;
            }
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            ((User) userObject).setName(text);
            //((Workflow)userObject).setUploader(muser);
            //System.out.println("Author Name: "+text);
        }
    }

    //rule for the date the workflow was created/uploaded
    public static class DateRule extends DefaultRule {

        public DateRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowDatecreated(text);
            ((Workflow) userObject).setWorkflowDatecreated(text);

        }
    }

    public static class PreviewRule extends DefaultRule {

        public PreviewRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowPreview(text);
            ((Workflow) userObject).setWorkflowPreview(text);
        }
    }

    public static class LicenceTypeRule extends DefaultRule {

        public LicenceTypeRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {
            super.handleParsedAttribute(parser, index, value, userObject);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowLicenceType("Licence By " + text);
            ((Workflow) userObject).setWorkflowLicenceType("Licence By " + text);
        }
    }

    //set download link for the workflow
    public static class ContentUriRule extends DefaultRule {

        public ContentUriRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowRemoteUrl(text);
            ((Workflow) userObject).setWorkflowRemoteUrl(text);
        }
    }

    public static class ContentTypeRule extends DefaultRule {

        public ContentTypeRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowContentType(text);
            ((Workflow) userObject).setWorkflowContentType(text);
        }
    }

    public static class TagsRule extends DefaultRule {

        public TagsRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {
            super.handleParsedAttribute(parser, index, value, userObject);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflowTags(new ArrayList<String>() {
            });
            ((Workflow) userObject).setWorkflowTags(new ArrayList<String>() {
            });

        }
    }

    /**
     * parse workflows from myExperiment
     */
    public static final class WorkflowRule extends DefaultRule {
        static String uri, version, desc;
        static String url = uri = version = desc = "";
        static long id = 0;
        Workflow workflow;
        List<Workflow> wlist;

        public WorkflowRule(Type type, String locationPath, String... attributeNames) throws
                IllegalArgumentException {
            super(type, locationPath, attributeNames);
            this.workflow = new Workflow();
            wlist = new ArrayList<>();
        }

        //instantiated to parse xml data for a given workflow
        public WorkflowRule(Type type, String path, int id, String attributenames) {
            super(type, path, attributenames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object
                userObject) {

            switch (index) {
                case 0:
                    desc = "To view workflow on the web, click " + value;
                    break;
                case 1:
                    uri = value;
                    break;
                case 2:
                    id = Integer.parseInt(value);
                    break;
                case 3:
                    version = value;
                    break;
            }
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object
                workflowListObject) {
            //add the  workflow to the workflow list
            this.workflow = new Workflow("", desc, id, url);
            this.workflow.setWorkflowDetailsUrl(uri);
            this.workflow.setWorkflowTitle(text);
            this.workflow.setWorkflowAuthor("");
            wlist.add(this.workflow);
            //WorkflowLoader.loadedWorkflows.add(this.workflow);
            ((List<Workflow>) workflowListObject).add(this.workflow);
            this.workflow = null;
        }
    }

}
