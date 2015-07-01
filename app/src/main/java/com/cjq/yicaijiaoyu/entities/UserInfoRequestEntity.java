package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/30.
 */
public class UserInfoRequestEntity {
    String code;
    Data data;

    public UserInfoRequestEntity() {
    }

    public UserInfoRequestEntity(String code, Data data) {
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
        String userId;

        public Data() {
        }

        public Data(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
