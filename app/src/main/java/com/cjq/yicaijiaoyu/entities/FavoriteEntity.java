package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/7/2.
 */
public class FavoriteEntity {
    String code;
    Data data;

    public FavoriteEntity(String code, Data data) {
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
        String goods_id;

        public Data(String userId, String goods_id) {
            this.userId = userId;
            this.goods_id = goods_id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }
    }
}
