package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.CourseEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by CJQ on 2015/7/1.
 */
public class AdUtil {

    public static void chargeAdList(JSONArray goods,List<CourseEntity> courseEntityList,Context context) throws JSONException {
        for(int i=0;i<goods.length();i++){
            JSONObject o = goods.getJSONObject(i);
            CourseEntity e= new CourseEntity(o.getString("url"), CommonDataObject.MAIN_URL+o.getString("image"),o.getString("ad_name"),null,null,getBanner(context),null,false,null);
            courseEntityList.add(e);
        }
    }

    private static View getBanner(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.recommend_course,null);
    }
}
