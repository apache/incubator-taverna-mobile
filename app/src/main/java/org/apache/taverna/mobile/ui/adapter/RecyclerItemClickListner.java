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
package org.apache.taverna.mobile.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerItemClickListner implements RecyclerView.OnItemTouchListener {
 
    protected OnItemClickListener listener;
 
    private GestureDetector gestureDetector;
 
    @Nullable
    private View childView;
 
    private int childViewPosition;
 
    public RecyclerItemClickListner(Context context, OnItemClickListener listener) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    } 
 
    @Override 
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent event) {
        childView = view.findChildViewUnder(event.getX(), event.getY());
        childViewPosition = view.getChildAdapterPosition(childView);
 
        return childView != null && gestureDetector.onTouchEvent(event);
    } 
 
    @Override 
    public void onTouchEvent(RecyclerView view, MotionEvent event) {
        // Not needed. 
    } 
 
    @Override 
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
 
    } 
 
    /** 
     * A click listener for items. 
     */ 
    public interface OnItemClickListener { 
 
        /** 
         * Called when an item is clicked. 
         * 
         * @param childView View of the item that was clicked. 
         * @param position  Position of the item that was clicked. 
         */ 
        void onItemClick(View childView, int position);
 
        /** 
         * Called when an item is long pressed. 
         * 
         * @param childView View of the item that was long pressed. 
         * @param position  Position of the item that was long pressed. 
         */ 
        void onItemLongPress(View childView, int position);
 
    } 
 
    /** 
     * A simple click listener whose methods can be overridden one by one. 
     */ 
    public static abstract class SimpleOnItemClickListener implements OnItemClickListener { 
 
        /** 
         * Called when an item is clicked. The default implementation is a no-op. 
         * 
         * @param childView View of the item that was clicked. 
         * @param position  Position of the item that was clicked. 
         */ 
        public void onItemClick(View childView, int position) {
            // Do nothing. 
        } 
 
        /** 
         * Called when an item is long pressed. The default implementation is a no-op. 
         * 
         * @param childView View of the item that was long pressed. 
         * @param position  Position of the item that was long pressed. 
         */ 
        public void onItemLongPress(View childView, int position) {
            // Do nothing. 
        } 
 
    } 
 
    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {
 
        @Override 
        public boolean onSingleTapUp(MotionEvent event) {
            if (childView != null) {
                listener.onItemClick(childView, childViewPosition);
            } 
 
            return true; 
        } 
 
        @Override 
        public void onLongPress(MotionEvent event) {
            if (childView != null) {
                listener.onItemLongPress(childView, childViewPosition);
            } 
        } 
 
        @Override 
        public boolean onDown(MotionEvent event) {
            // Best practice to always return true here. 
            // http://developer.android.com/training/gestures/detector.html#detect 
            return true; 
        } 
 
    } 
 
}