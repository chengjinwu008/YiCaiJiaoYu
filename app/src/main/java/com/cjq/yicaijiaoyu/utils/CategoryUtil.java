package com.cjq.yicaijiaoyu.utils;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.entities.CategoryEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by CJQ on 2015/7/1.
 */
public class CategoryUtil {

    public static void chargeAdList(JSONArray goods,List<CategoryEntity> categoryEntityList) throws JSONException {
        for(int i=0;i<goods.length();i++){
            JSONObject o = goods.getJSONObject(i);
            CategoryEntity e= new CategoryEntity(CommonDataObject.MAIN_URL+ "/"+o.getString("cat_logo"),o.getString("name"),o.getString("id"));
            categoryEntityList.add(e);
        }
    }
}
