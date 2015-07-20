package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.content.Intent;

import com.cjq.yicaijiaoyu.activities.LoginActivity;
import com.cjq.yicaijiaoyu.activities.MainActivity;

/**
 * Created by CJQ on 2015/6/26.
 */
public class AccountUtil {

    public static boolean isLoggedIn(Context context){
        String id = getUserId(context);
        return  id!= null && !"0".equals(id);
    }

    public static String getUserId(Context context){
        String name=SharedPreferenceUtil.getInstance(context).readString(SharedPreferenceUtil.USER_ID);
        if(name==null)
            return "0";
        return name;
    }

    public static void doLogin(){
        //todo  实现登录
    }

    public static void doLogout(){
        //todo 实现登出

    }

    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
