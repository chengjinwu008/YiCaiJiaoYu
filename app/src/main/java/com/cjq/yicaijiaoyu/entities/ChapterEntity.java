package com.cjq.yicaijiaoyu.entities;

import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class ChapterEntity {
    String name;
    String id;
    List<CourseEntity> videos;

    public ChapterEntity(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CourseEntity> getVideos() {
        return videos;
    }

    public void setVideos(List<CourseEntity> videos) {
        this.videos = videos;
    }

    public ChapterEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
