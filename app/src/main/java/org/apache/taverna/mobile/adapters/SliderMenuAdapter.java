package org.apache.taverna.mobile.adapters;
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

import org.apache.taverna.mobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 6/7/15.
 */
public class SliderMenuAdapter extends BaseAdapter {

    private List<String> dataItems;
    private Context context;

    public SliderMenuAdapter(Context c, List<String> items) {
        dataItems = items;
        context = c;
    }

    @Override
    public int getCount() {
        return dataItems.size();
    }

    @Override
    public String getItem(int i) {
        return dataItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View menuitemview = LayoutInflater.from(context).inflate(R.layout.menu_item_layout,
                viewGroup, false);

        ImageView menuicon = (ImageView) menuitemview.findViewById(R.id.menuIcon);
        TextView menuitem = (TextView) menuitemview.findViewById(R.id.menuItemText);
        switch (i + 1) {
            case 1:
                menuicon.setImageResource(R.mipmap.ic_dashboard_home);
                menuitem.setText(dataItems.get(i));
                break;
            case 2:
                menuicon.setImageResource(R.mipmap.ic_openwk);
                menuitem.setText(dataItems.get(i));
                break;
            case 3:
                menuicon.setImageResource(R.mipmap.ic_usage);
                menuitem.setText(dataItems.get(i));
                break;
            case 4:
                menuicon.setImageResource(R.mipmap.ic_about);
                menuitem.setText(dataItems.get(i));
                break;
            case 5:
                menuicon.setImageResource(R.mipmap.ic_workflows);
                menuitem.setText(dataItems.get(i));
                break;
            case 6:
                menuicon.setImageResource(R.mipmap.ic_logout);
                menuitem.setText(dataItems.get(i));
                break;
        }
        return menuitemview;
    }

    public static class ViewHolder {
        public final ImageView menuicon;
        public final TextView menuitem;

        public ViewHolder(View view) {
            menuicon = (ImageView) view.findViewById(R.id.menuIcon);
            menuitem = (TextView) view.findViewById(R.id.menuItemText);
        }

    }
}
