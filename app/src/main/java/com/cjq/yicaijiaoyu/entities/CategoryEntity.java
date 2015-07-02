package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/24.
 */
public class CategoryEntity {
    String id;
    String imageId;
    String name;
    int imageResource;

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public CategoryEntity(String id, String imageId, String name, int imageResource) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.imageResource = imageResource;
    }

    public CategoryEntity(String imageId, String name,String id) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
