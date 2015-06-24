package com.cjq.yicaijiaoyu.entities;

/**
 * Created by android on 2015/6/23.
 */
public enum CourseCategory {

    FOR_JOB("会计从业"),FOR_PRIMARY("初级职称"),FOR_INTERMEDIATE("中级职称");

    private String name;


    private CourseCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
