package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/7/14.
 */
public class LectureRequestEntity {
    String code;
    Data data;

    public LectureRequestEntity(String code, Data data) {
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
        String teaId;
        String userId;

        public Data(String teaId, String userId) {
            this.teaId = teaId;
            this.userId = userId;
        }

        public String getTeaId() {
            return teaId;
        }

        public void setTeaId(String teaId) {
            this.teaId = teaId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
