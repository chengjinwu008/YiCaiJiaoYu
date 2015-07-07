package com.cjq.yicaijiaoyu.entities;

import java.util.Date;

/**
 * Created by CJQ on 2015/7/6.
 */
public class TeamDataEntity {
    String id;
    String name;
    String captain;
    String category;
    Date date;
    String info;
    int rank;
    int memberCount;
    String portrait;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public TeamDataEntity(String id, String name, String captain, String category, Date date, String info, int rank, int memberCount, String portrait) {
        this.id = id;
        this.name = name;
        this.captain = captain;
        this.category = category;
        this.date = date;
        this.info = info;
        this.rank = rank;
        this.memberCount = memberCount;
        this.portrait = portrait;
    }

    public TeamDataEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}
