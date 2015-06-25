package com.cjq.yicaijiaoyu.entities;

/**
 * Created by android on 2015/6/23.
 */
public class LectureEntity {
    String portrait_url;
    String intro;
    String name;
    int id;

    public LectureEntity(String name, String intro, String portrait_url) {
        this.name = name;
        this.intro = intro;
        this.portrait_url = portrait_url;
    }

    public LectureEntity() {
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
