package com.cjq.yicaijiaoyu.entities;

import android.view.View;

/**
 * Created by android on 2015/6/23.
 */
public class CourseEntity {

    public CourseEntity(String cover_image_url, String title, CourseCategory category, boolean free) {
        this.cover_image_url = cover_image_url;
        this.title = title;
        this.category = category;
        this.free = free;
    }

    public CourseEntity(String requestApi, String cover_image_url, String title, LectureEntity lecture, CourseCategory category, View view, String intro, boolean free, int id) {
        this.requestApi = requestApi;
        this.cover_image_url = cover_image_url;
        this.title = title;
        this.lecture = lecture;
        this.category = category;
        this.view = view;
        this.intro = intro;
        this.free = free;
        this.id = id;
    }

    public CourseEntity() {
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    int progress;
    String requestApi;
    String cover_image_url;
    String title;
    LectureEntity lecture;
    CourseCategory category;

    public String getRequestApi() {
        return requestApi;
    }

    public void setRequestApi(String requestApi) {
        this.requestApi = requestApi;
    }

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
