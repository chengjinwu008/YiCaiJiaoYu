package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/29.
 */
public class RegisterRequestEntity {
    String code;
    Data data;

    public RegisterRequestEntity(String code, Data data) {
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
        String phoneNumber;
        String password;
        String smsCode;

        public Data(String phoneNumber, String password, String smsCode) {
            this.phoneNumber = phoneNumber;
            this.password = password;
            this.smsCode = smsCode;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSmsCode() {
            return smsCode;
        }

        public void setSmsCode(String smsCode) {
            this.smsCode = smsCode;
        }
    }
}
