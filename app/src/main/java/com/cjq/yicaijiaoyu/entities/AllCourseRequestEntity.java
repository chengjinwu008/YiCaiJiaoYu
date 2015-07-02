package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/7/1.
 */
public class AllCourseRequestEntity {

    String code;
    Data data;

    public AllCourseRequestEntity(String code, Data data) {
        this.code = code;
        this.data = data;
    }

    public AllCourseRequestEntity() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        String userId = "0";
        String catId = "0";
        int page;
        int num;
        String isfree="";
        String keyword;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public Data(String isfree, int page, int num) {
            this.isfree = isfree;
            this.page = page;
            this.num = num;
        }

        public Data(String catId, int page, int num, String isfree) {
            this.catId = catId;
            this.page = page;
            this.num = num;
            this.isfree = isfree;
        }

        public Data(int page, int num) {
            this.page = page;
            this.num = num;
        }

        public Data() {
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
