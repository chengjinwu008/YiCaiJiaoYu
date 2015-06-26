package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/26.
 */
public class UserEntity {
    String protrait_url;
    String name;
    String id;

    public UserEntity() {
    }

    public UserEntity(String protrait_url, String name, String id) {
        this.protrait_url = protrait_url;
        this.name = name;
        this.id = id;
    }

    public String getProtrait_url() {
        return protrait_url;
    }

    public void setProtrait_url(String protrait_url) {
        this.protrait_url = protrait_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
