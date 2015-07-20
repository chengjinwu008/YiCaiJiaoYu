package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/7/2.
 */
public class CourseInfoRequestEntity {
    String code;
    Data data;

    public CourseInfoRequestEntity(String code, Data data) {
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

    public static class Data {
        String goods_id;
        String userId;

        public Data(String goods_id, String userId) {
            this.goods_id = goods_id;
            this.userId = userId;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
