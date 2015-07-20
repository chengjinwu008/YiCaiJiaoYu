package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.dao.Course;

/**
 * Created by CJQ on 2015/6/25.
 */
public class VideoInfoEvent {
   Course course;

    public VideoInfoEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
