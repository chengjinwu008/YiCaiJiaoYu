package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/25.
 */
public class VideoInfoEvent {
    String portraitUrl;
    String lecName;
    String lecInfo;
    String videoInfo;

    public VideoInfoEvent(String portraitUrl, String lecName, String lecInfo, String videoInfo) {
        this.portraitUrl = portraitUrl;
        this.lecName = lecName;
        this.lecInfo = lecInfo;
        this.videoInfo = videoInfo;
    }

    public VideoInfoEvent() {
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getLecName() {
        return lecName;
    }

    public void setLecName(String lecName) {
        this.lecName = lecName;
    }

    public String getLecInfo() {
        return lecInfo;
    }

    public void setLecInfo(String lecInfo) {
        this.lecInfo = lecInfo;
    }

    public String getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }
}
