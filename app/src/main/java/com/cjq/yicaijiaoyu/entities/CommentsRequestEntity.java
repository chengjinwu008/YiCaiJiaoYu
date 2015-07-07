package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.CommonDataObject;

/**
 * Created by CJQ on 2015/7/7.
 */
public class CommentsRequestEntity {
    String code;
    Data data;

    public CommentsRequestEntity(String code, Data data) {
        this.code = code;
        this.data = data;
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

    public static class Data{
        int page;
        int limit = CommonDataObject.COURSE_NUM_SHOWING;
        String gid;
        String userId;

        public Data(int page, String gid, String userId) {
            this.page = page;
            this.gid = gid;
            this.userId = userId;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
