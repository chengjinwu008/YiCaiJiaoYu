package com.cjq.yicaijiaoyu.entities;

/**
 * Created by CJQ on 2015/6/30.
 */
public class LoginRequestEntity {

    String code;
    Data data;

    public LoginRequestEntity(String code, Data data) {
        this.code = code;
        this.data = data;
    }

    public LoginRequestEntity() {
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
        String userName;
        String phoneNumber;
        String password;

        public Data() {
        }

        public Data(String userName, String phoneNumber, String password) {
            this.userName = userName;
            this.phoneNumber = phoneNumber;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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
    }
}
