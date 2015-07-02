package com.cjq.yicaijiaoyu.utils;

import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.LectureEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by CJQ on 2015/7/1.
 */
public class CourseUtil {

    public static void chargeCourseList(JSONArray goods,List<CourseEntity> courseEntityList) throws JSONException {
        for(int i=0;i<goods.length();i++){
            JSONObject o = goods.getJSONObject(i);
            LectureEntity l = new LectureEntity(o.getString("keywords"),null,null);
            CourseEntity e= new CourseEntity(o.getString("url"),null,o.getString("name"),l,o.getString("brief"),null,null,o.getInt("is_free")==1,o.getString("id"));
            if(o.has("thnumb"))
            e.setCover_image_url(o.getString("thumb"));
            else if(o.has("goods_img"))
            e.setCover_image_url(o.getString("goods_img"));
            courseEntityList.add(e);
        }
    }
}
