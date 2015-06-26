package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/26.
 */
public class SettingEntity {
    String title;
    String declare="";
    boolean canJump=false;

    public SettingEntity(String title) {
        this.title = title;
    }

    public SettingEntity(String title, String declare, boolean canJump) {
        this.title = title;
        this.declare = declare;
        this.canJump = canJump;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }
}
