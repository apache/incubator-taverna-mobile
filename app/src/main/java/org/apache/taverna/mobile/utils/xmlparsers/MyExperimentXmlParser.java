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

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.rule.DefaultRule;

import org.apache.taverna.mobile.tavernamobile.Workflow;
import org.apache.taverna.mobile.utils.WorkflowLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Akah on 6/23/15.
 */
public class MyExperimentXmlParser {

    //parse a single workflow from myexperiment
    public static final class WorkflowItemRule extends DefaultRule{

        /**
         * Create a new rule with the given values.
         *
         * @param type           The type of the rule.
         * @param locationPath   The location path of the element to target in the XML.
         * @param attributeNames An optional list of attribute names to parse values for if the
         *                       type of this rule is {@link com.thebuzzmedia.sjxp.rule.IRule.Type#ATTRIBUTE}.
         * @throws IllegalArgumentException if <code>type</code> is <code>null</code>, if
         *                                            <code>locationPath</code> is <code>null</code> or empty, if
         *                                            <code>type</code> is {@link com.thebuzzmedia.sjxp.rule.IRule.Type#ATTRIBUTE} and
         *                                            <code>attributeNames</code> is <code>null</code> or empty or
         *                                            if <code>type</code> is {@link com.thebuzzmedia.sjxp.rule.IRule.Type#CHARACTER} and
         *                                            <code>attributeNames</code> <strong>is not</strong>
         *                                            <code>null</code> or empty.
         */
        public WorkflowItemRule(Type type, String locationPath, String... attributeNames) throws IllegalArgumentException {
            super(Type.CHARACTER, "http://www.myexperiment.org/workflows.xml", "workflow");
        }

        /**
         * Default no-op implementation. Please override with your own logic.
         *
         * @param parser
         * @param index
         * @param value
         * @param userObject
         * @see com.thebuzzmedia.sjxp.rule.IRule#handleParsedAttribute(com.thebuzzmedia.sjxp.XMLParser, int, String, Object)
         */
        @Override
        public void handleParsedAttribute(XMLParser parser, int index, String value, Object userObject) {
            switch(index){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;

            }
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
                    System.out.println("Workflow Resource: "+value); url = value;
                    desc = "To view workflow on the web, click "+value;
                    break;
                case 1:
                    System.out.println("Workflow uri: "+value);
                    uri = value;
                    break;
                case 2:
                    System.out.println("Workflow id: "+value);
                    id = Integer.parseInt(value);
                    break;
                case 3:
                    System.out.println("Workflow version: "+value);
                    version = value;
                    break;
            }
        }

        @Override
        public void handleParsedCharacters(XMLParser parser, String text, Object workflowListObject) {
            //add the  workflow to the workflow list
            this.workflow = new Workflow("", desc, id, url);
            this.workflow.setWorkflow_title(text);
            this.workflow.setWorkflow_author("");
            wlist.add(this.workflow);
            WorkflowLoader.loadedWorkflows.add(this.workflow);
            System.out.println("static Workflow Count: " + WorkflowLoader.loadedWorkflows.size());
            ((List<Workflow>)workflowListObject).add(this.workflow);
            this.workflow = null;
        }

    }

}
