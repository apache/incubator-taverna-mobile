package org.apache.taverna.mobile.utils.xmlparsers;

import com.thebuzzmedia.sjxp.XMLParser;
import com.thebuzzmedia.sjxp.XMLParserException;
import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.fragments.WorkflowItemFragment;
import org.apache.taverna.mobile.fragments.workflowdetails.WorkflowdetailFragment;
import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.tavernamobile.Workflow;

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

/**
 * Parse details from the xml output of myexperiment API
 * Created by Larry Akah on 6/24/15.
 */
public class WorkflowDetailParser extends XMLParser {

    public WorkflowDetailParser(IRule[] rules) throws IllegalArgumentException, XMLParserException {
        super(rules);
    }

    //deliver results when parsing has completed and all the information required has been retrieved
    @Override
    protected void doEndDocument(Object userObject) {
        if (userObject instanceof User) {
            WorkflowItemFragment.startLoadingAvatar((User) userObject);
        } else {
            WorkflowdetailFragment.setWorkflowDetails((Workflow) userObject);
        }
    }
}
