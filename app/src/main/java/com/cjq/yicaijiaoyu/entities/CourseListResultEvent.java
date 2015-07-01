package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;

/**
 * Created by CJQ on 2015/6/29.
 */
public class CourseListResultEvent {
    CourseListAdapter adapter;
    String requestCode;

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public CourseListResultEvent(CourseListAdapter adapter, String requestCode) {
        this.adapter = adapter;
        this.requestCode = requestCode;
    }

    public CourseListResultEvent() {
    }

    public CourseListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CourseListAdapter adapter) {
        this.adapter = adapter;
    }
}
