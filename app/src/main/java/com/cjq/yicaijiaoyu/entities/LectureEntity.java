package com.cjq.yicaijiaoyu.entities;

/**
 * Created by android on 2015/6/23.
 */
public class LectureEntity {
    String portraint_url;
    String intro;
    String name;
    int id;

    public LectureEntity(String name, String intro, String portraint_url) {
        this.name = name;
        this.intro = intro;
        this.portraint_url = portraint_url;
    }

    public LectureEntity() {
    }

    public String getPortraint_url() {
        return portraint_url;
    }

    public void setPortraint_url(String portraint_url) {
        this.portraint_url = portraint_url;
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
