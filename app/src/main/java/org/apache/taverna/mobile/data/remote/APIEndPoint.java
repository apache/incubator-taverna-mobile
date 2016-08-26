/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.taverna.mobile.data.remote;

//This class contains all the Constants for API End Points

public class APIEndPoint {

    public static final String ALL_ANNOUNCEMENT = "announcements.xml";
    public static final String ANNOUNCEMENT = "announcement.xml";
    public static final String ALL_WORKFLOW = "workflows.xml";
    public static final String WORKFLOW = "workflow.xml";
    public static final String USER = "user.xml";
    public static final String LICENSE = "license.xml";

    public static final String WHOAMI = "whoami.xml";

    public static final String XML_ACCEPT_HEADER = "Accept: application/xml";
    public static final String JSON_CONTENT_HEADER = "Content-Type: application/json";
    public static final String UTF_CONTENT_ENCODING_HEADER = "Content-Encoding: UTF-8";
}
