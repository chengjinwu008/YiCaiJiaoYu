package com.cjq.yicaijiaoyu.entities;

import android.content.Context;

import com.cjq.yicaijiaoyu.R;

/**
 * Created by CJQ on 2015/7/2.
 */
public class AdviseRequestEntity {
    String code;
    Data data;

    public AdviseRequestEntity(String code, Data data) {
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
        String msg_title;
        String msg_content;

        public Data(String userId, String msg_content,Context context) {
            this.userId = userId;
            this.msg_content = msg_content;
            setMsg_title(context.getResources().getString(R.string.advises));
        }

        public String getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(String msg_content) {
            this.msg_content = msg_content;
        }

        public String getMsg_title() {
            return msg_title;
        }

        public void setMsg_title(String msg_title) {
            this.msg_title = msg_title;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }






}
