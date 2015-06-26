package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/5/5.
 */
public class SharedPreferenceUtil {
    private SharedPreferences preferences;
    private static SharedPreferenceUtil INSTANCE = null;
    public static final String ACCOUNT ="account";
    public static final String USER_ID="user_id";

    public static SharedPreferenceUtil getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new SharedPreferenceUtil(context,ACCOUNT);
        }
        return INSTANCE;
    }

    private SharedPreferenceUtil(Context context,String name) {
        preferences = context.getApplicationContext().getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public String readString(String key){
        return preferences.getString(key,null);
    }

    public void putString(String[] keys,String[] values){
        if(keys==null||values==null)
            return;
        if(keys.length!=values.length)
            return;
        SharedPreferences.Editor editor = preferences.edit();

        for(int i = 0 ;i<keys.length;i++){
            editor.putString(keys[i],values[i]);
        }
        editor.apply();
    }

    public void delete(String[] keys){
        if(keys==null)
            return;
        if(keys.length==0)
            return;
        SharedPreferences.Editor editor = preferences.edit();

        for (String key : keys) {
            editor.remove(key);
        }

        editor.apply();
    }
}
