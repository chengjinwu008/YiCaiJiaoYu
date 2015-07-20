package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.dao.Course;

/**
 * Created by CJQ on 2015/6/26.
 */
public class CommentsResultEvent {

    Course course;

    public CommentsResultEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
