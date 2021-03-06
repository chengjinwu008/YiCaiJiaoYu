package com.cjq.yicaijiaoyu.entities;

import android.net.wifi.WifiConfiguration;

/**
 * Created by CJQ on 2015/7/6.
 */
public class CommentCommitRequestEntity {

    String code;
    Data data;


    public CommentCommitRequestEntity(String code, Data data) {
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
        String goods_id;
        String content;
        int status = 0;
        String userId;

        public Data(String goods_id, String content, String userId) {
            this.goods_id = goods_id;
            this.content = content;
            this.userId = userId;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


}
