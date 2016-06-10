package org.apache.taverna.mobile.utils;
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

import com.thebuzzmedia.sjxp.rule.IRule;

import org.apache.taverna.mobile.adapters.WorkflowAdapter;
import org.apache.taverna.mobile.tavernamobile.User;
import org.apache.taverna.mobile.utils.xmlparsers.AvatarXMLParser;
import org.apache.taverna.mobile.utils.xmlparsers.MyExperimentXmlParserRules;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * initiates a process to fetch and parse uploader information so as to retrieve the id, name and
 * avatar link
 * Created by Larry AKah on 6/29/15.
 */
public class AvatarLoader extends AsyncTask<String, Void, Void> {
    WorkflowAdapter.ViewHolder vh;

    public AvatarLoader(WorkflowAdapter.ViewHolder userViewHolder) {
        this.vh = userViewHolder;
    }

    @Override
    protected Void doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection connection = null;
        try {

            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            IRule avatarRule = new MyExperimentXmlParserRules.AuthorRule(IRule.Type.ATTRIBUTE,
                    "/user/avatar", "resource", "uri", "id");
            IRule avatarName = new MyExperimentXmlParserRules.AuthorRule(IRule.Type.CHARACTER,
                    "/user/name");
            AvatarXMLParser avatarXMLParser = new AvatarXMLParser(new IRule[]{avatarRule,
                    avatarName});

            avatarXMLParser.parse(input, new User(strings[1], this.vh));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
