package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/29.
 */
public class SMSRequestEntity {
    String code;
    public Data data;

    public SMSRequestEntity(String code, Data data) {
        this.code = code;
        this.data = data;
    }

    public SMSRequestEntity() {
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
        String phoneNumber;

        public Data(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Data() {
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
