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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * This class contains all utility functions used by our database for basic functionality not
 * directly related to the core
 * functionality of it but that aids a core functionality to carryout its function efficiently.
 * This class is designed to be a singleton class
 *
 * @author Larry Akah
 */
public class DBUtility {

    /**
     * Returns a new JSONArray of new key values to be stored ommitting the identified entry to be
     * removed
     */
    public static JSONArray removeKey(JSONArray keys, String removeid) throws JSONException {
        ArrayList<String> mkeys = new ArrayList<String>();
        if (keys != null) {
            for (int i = 0; i < keys.length(); i++) {
                mkeys.add(keys.getString(i));
            }
            mkeys.remove(removeid);
            JSONArray nkeyArray = new JSONArray();
            for (String newkey : mkeys) {
                nkeyArray.put(newkey);
            }
            return nkeyArray; //returns the new keys to save under the db;

        } else {
            return keys; //returns a null value indicating nothin to remove due to no keys available
        }

    }

}
