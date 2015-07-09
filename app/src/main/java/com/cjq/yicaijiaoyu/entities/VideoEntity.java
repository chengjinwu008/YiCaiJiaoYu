package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/7/2.
 */
public class VideoEntity {
    // TODO: 2015/7/2 实现视频实体 记得修改章节列表包含的实体变更为视频实体！！！
    String vid;
    String name;
    long seek;

    public VideoEntity() {
    }

    public VideoEntity(String vid, String name, long seek) {
        this.vid = vid;
        this.name = name;
        this.seek = seek;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSeek() {
        return seek;
    }

    public void setSeek(long seek) {
        this.seek = seek;
    }
}
