package com.cjq.yicaijiaoyu.entities;

import android.view.View;

/**
 * Created by android on 2015/6/23.
 */
public class CourseEntity {

    String cover_image_url;
    String title;
    LectureEntity lecture;
    CourseCategory category;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    View view;

    String intro;
    int id;
    boolean free;

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LectureEntity getLecture() {
        return lecture;
    }

    public void setLecture(LectureEntity lecture) {
        this.lecture = lecture;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
