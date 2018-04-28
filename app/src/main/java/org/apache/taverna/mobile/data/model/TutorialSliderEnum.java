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
package org.apache.taverna.mobile.data.model;

import org.apache.taverna.mobile.R;

public enum TutorialSliderEnum {

    SLIDE1(R.layout.tutorial_slide1),
    SLIDE2(R.layout.tutorial_slide2),
    SLIDE3(R.layout.tutorial_slide3),
    SLIDE4(R.layout.tutorial_slide4);

    private int layoutId;

    TutorialSliderEnum(int layout_Id) {
        layoutId = layout_Id;
    }

    public int getLayoutId() {
        return layoutId;
    }
}
