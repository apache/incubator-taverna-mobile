package org.apache.taverna.mobile.utils.xmlparsers;
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

import android.text.Html;

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.rule.DefaultRule;

import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.WorkflowLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/23/15.
 */
public class MyExperimentXmlParserRules {

    public static Workflow mWorkflow = new Workflow();

    public MyExperimentXmlParserRules(){
    }

    public Workflow getWorkflowHere(){
        return mWorkflow;
    }
    //parse a single workflow from myexperiment
    public final static class WorkflowDetailRule extends DefaultRule{

        public WorkflowDetailRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            switch(index){
                case 0: //uri
                    mWorkflow.setWorkflow_details_url(value);
                    ((Workflow)userObject).setWorkflow_details_url(value);
                    break;
                case 1: //resource
                    mWorkflow.setWorkflow_web_url(value);
                    ((Workflow)userObject).setWorkflow_web_url(value);
                    break;
                case 2: //id
                    mWorkflow.setId(Integer.parseInt(value));
                    ((Workflow)userObject).setId(Integer.parseInt(value));
                    break;
                case 3://version
                    mWorkflow.setWorkflow_versions(value);
                    ((Workflow)userObject).setWorkflow_versions(value);
                    break;
            }
        }
    }

    public static class TitleRule extends DefaultRule{

        public TitleRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflow_title(text);
            ((Workflow)userObject).setWorkflow_title(text);
        }
    }

    public static class DescriptionRule extends DefaultRule{

        public DescriptionRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {

            mWorkflow.setWorkflow_description(String.valueOf(Html.fromHtml(text)));
            ((Workflow)userObject).setWorkflow_description(String.valueOf(Html.fromHtml(text)));
        }
    }

    public static class TypeRule extends DefaultRule{

        public TypeRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            switch (index){
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
            mWorkflow.setWorkflow_Type(text);
            ((Workflow)userObject).setWorkflow_Type(text);
        }
    }

    public static class UploaderRule extends DefaultRule{

        User muser;
        public UploaderRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
            muser = new User("", null);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            switch(index){
                case 0:
                    muser.setWebsite(value);
                    if( (userObject instanceof User)){
                        ((User)userObject).setWebsite(value);
                    }
                    break;
                case 1:
                    muser.setDetails_uri(value);
                    if( (userObject instanceof User)){
                        ((User)userObject).setDetails_uri(value);
                    }
                    break;
                case 2:
                    muser.setId(value);
                    if( (userObject instanceof User)){
                        ((User)userObject).setId(value);
                    }
                    break;
            }
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
           muser.setName(text);
           mWorkflow.setUploader(muser);

            if( (userObject instanceof User)){
                ((User)userObject).setName(text);
            }else{
                ((Workflow)userObject).setUploader(muser);
            }
        }
    }
    //rule used to parse author from main page
    public static class AuthorRule extends DefaultRule{

        public AuthorRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            switch(index){
                case 0:
                    ((User) userObject).setAvatar_url(value);
                    break;
                case 1:
                    ((User) userObject).setDetails_uri(value);
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
    public static class DateRule extends DefaultRule{

        public DateRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

    @Override
    public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
        mWorkflow.setWorkflow_datecreated(text);
        ((Workflow)userObject).setWorkflow_datecreated(text);

    }
}

    public static class PreviewRule extends DefaultRule{

        public PreviewRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflow_preview(text);
            ((Workflow)userObject).setWorkflow_preview(text);
        }
    }

    public static class LicenceTypeRule extends DefaultRule{

        public LicenceTypeRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            super.handleParsedAttribute(parser, index, value, userObject);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflow_licence_type("Licence By "+text);
            ((Workflow)userObject).setWorkflow_licence_type("Licence By "+text);
        }
    }
    //set download link for the workflow
    public static class ContentUriRule extends DefaultRule{

        public ContentUriRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflow_remote_url(text);
            ((Workflow)userObject).setWorkflow_remote_url(text);
        }
    }

    public static class ContentTypeRule extends DefaultRule{

        public ContentTypeRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflow_content_type(text);
            ((Workflow)userObject).setWorkflow_content_type(text);
        }
    }

    public static class TagsRule extends DefaultRule{

        public TagsRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            super.handleParsedAttribute(parser, index, value, userObject);
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object userObject) {
            mWorkflow.setWorkflow_tags(new ArrayList<String>(){});
            ((Workflow)userObject).setWorkflow_tags(new ArrayList<String>(){});

        }
    }

    /**
     * parse workflows from myExperiment
     */
    public final static class WorkflowRule extends DefaultRule{
        Workflow workflow;
        List<Workflow>  wlist;
        static String uri,version,desc;
        static String url=uri=version=desc="";
        static long id = 0;

        public WorkflowRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(type, locationPath, attributeNames);
            this.workflow = new Workflow();
            wlist = new ArrayList<>();
        }
        //instantiated to parse xml data for a given workflow
        public WorkflowRule(Type type, String path, int id, String attributenames){
            super(type,path,attributenames);
        }

        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {

            switch(index){
                case 0:
                    desc = "To view workflow on the web, click "+value;
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
        public void handleParsedCharacters(XMLParser parser, String text, Object workflowListObject) {
            //add the  workflow to the workflow list
            this.workflow = new Workflow("", desc, id, url);
            this.workflow.setWorkflow_details_url(uri);
            this.workflow.setWorkflow_title(text);
            this.workflow.setWorkflow_author("");
            wlist.add(this.workflow);
            //WorkflowLoader.loadedWorkflows.add(this.workflow);
            ((List<Workflow>)workflowListObject).add(this.workflow);
            this.workflow = null;
        }
    }

}
