package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.CommonDataObject;

/**
 * Created by CJQ on 2015/7/17.
 */
public class CaredRequestEntity {

    String code= CommonDataObject.COURSE_CARE_REQUEST_CODE;

    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CaredRequestEntity(Data data) {
        this.data = data;
    }

    public static class Data{
        String userId;
        String page;
        String num;

        public Data(String userId, String page, String num) {
            this.userId = userId;
            this.page = page;
            this.num = num;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
