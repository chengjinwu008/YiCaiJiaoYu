package com.cjq.yicaijiaoyu.utils;

import android.content.Context;

/**
 * Created by CJQ on 2015/6/26.
 */
public class AccountUtil {

    public static boolean isLoggedIn(Context context){
        return getUserId(context) != null;
    }

    public static String getUserId(Context context){
        return SharedPreferenceUtil.getInstance(context).readString(SharedPreferenceUtil.USER_ID);
    }

    public static void doLogin(){
        //todo  实现登录
    }

    public static void doLogout(){
        //todo 实现登出

    }
}
