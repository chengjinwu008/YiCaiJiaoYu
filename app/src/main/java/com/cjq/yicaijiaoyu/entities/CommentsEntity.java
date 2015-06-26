package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/26.
 */
public class CommentsEntity {
    long time;
    UserEntity user;
    String content;

    public CommentsEntity() {
    }

    public CommentsEntity(long time, UserEntity user, String content) {
        this.time = time;
        this.user = user;
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
