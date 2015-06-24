package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/24.
 */
public class CategoryEntity {

    int imageId;
    String name;

    public CategoryEntity(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
