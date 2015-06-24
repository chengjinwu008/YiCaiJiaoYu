package com.cjq.yicaijiaoyu.entities;

/**
 * Created by android on 2015/6/23.
 */
public class MenuItemEntity {
    String titleText;
    int icon_id;
    int icon_selected_id;

    public MenuItemEntity(String titleText, int icon_id, int icon_selected_id) {
        this.titleText = titleText;
        this.icon_id = icon_id;
        this.icon_selected_id = icon_selected_id;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public int getIcon_selected_id() {
        return icon_selected_id;
    }

    public void setIcon_selected_id(int icon_selected_id) {
        this.icon_selected_id = icon_selected_id;
    }
}
