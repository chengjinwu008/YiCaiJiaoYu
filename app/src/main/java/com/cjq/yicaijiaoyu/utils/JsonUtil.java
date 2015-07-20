package com.cjq.yicaijiaoyu.utils;

import android.content.Context;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.dao.Ad;
import com.cjq.yicaijiaoyu.dao.Bought;
import com.cjq.yicaijiaoyu.dao.Cared;
import com.cjq.yicaijiaoyu.dao.Category;
import com.cjq.yicaijiaoyu.dao.Chapter;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.dao.Lecture;
import com.cjq.yicaijiaoyu.dao.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/7/14.
 */
public class JsonUtil {

    public static Course getFromCourseList(Context context,JSONObject o) throws JSONException {
        Course course = new Course();
        course.setGoods_id(o.getString("id"));
        course.setName(o.getString("name"));
        course.setAuthority(o.getInt("authority"));
        course.setGiftBag(o.getInt("giftBag") == 1);
        course.setCategoryId(o.getLong("categoryId"));
        course.setImage(o.getString("thumb"));
        course.setCategoryName(o.getString("brief"));
        String pre = CommonDataObject.MAIN_URL+o.getString("brandlogo");
        CourseHistoryUtil.addToCourseList(context,course);

        JSONArray ja = o.getJSONArray("lecture");
        ArrayList<Lecture> lectures=new ArrayList<>();
        for(int i=0;i<ja.length();i++){
            JSONObject oo = ja.getJSONObject(i);
            Lecture lecture = new Lecture();
            lecture.setName(oo.getString("brand_name"));
            lecture.setImage(pre + oo.getString("brand_logo"));
            lecture.setInfo(oo.getString("brand_desc"));
            lecture.setLectureId(oo.getString("brand_id"));
            lecture.setGoods_id(o.getString("id"));
            CourseHistoryUtil.addLecture(context, lecture);
            lectures.add(lecture);
        }
        course.setLectureList(lectures);
        return course;
    }

    public static Ad getAd(Context context,JSONObject o) throws JSONException {
        Ad ad = new Ad();
        ad.setImage(CommonDataObject.MAIN_URL + o.getString("image"));
        ad.setAd_id(o.getString("ad_id"));
        ad.setAd_name(o.getString("ad_name"));
        ad.setAd_link("http://" + o.getString("ad_link"));
        CourseHistoryUtil.addAd(context, ad);
        return ad;
    }

    public static Category getCategory(Context context,JSONObject o) throws JSONException{
        Category category = new Category();
        category.setCategoryId(o.getString("id"));
        category.setImage(CommonDataObject.MAIN_URL + File.separator + o.getString("cat_logo"));
        category.setName(o.getString("name"));
        CourseHistoryUtil.addCategory(context, category);
        return category;
    }

    public static Chapter getChapter(Context context,JSONObject o,String goods_id) throws JSONException {
        Chapter chapter = new Chapter();
        chapter.setName(o.getString("utit"));
        chapter.setChapter_id(o.getString("sid"));
        chapter.setGoods_id(goods_id);
        if(o.has("data")){
            JSONArray jsonArray = o.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject oo = jsonArray.getJSONObject(i);
                Video video  =new Video();
                video.setGoods_id(goods_id);
                video.setName(oo.getString("title"));
                video.setSeek(oo.getLong("seek"));
                video.setVid(oo.getString("vid"));
                video.setFree(oo.getInt("free") == 0);
                video.setGoods_id(goods_id);
                video.setChapter_id(chapter.getChapter_id());
                CourseHistoryUtil.addVideo(context,video);
            }
        }
        CourseHistoryUtil.addChapter(context, chapter);
        return chapter;
    }

    public static Course getFromCourseListNoCache(Context context,JSONObject o) throws JSONException {
        Course course = new Course();
        course.setGoods_id(o.getString("id"));
        course.setName(o.getString("name"));
        course.setAuthority(o.getInt("authority"));
        course.setGiftBag(o.getInt("giftBag") == 1);
        course.setCategoryId(o.getLong("categoryId"));
        course.setImage(o.getString("thumb"));
        course.setCategoryName(o.getString("brief"));
        String pre = CommonDataObject.MAIN_URL+o.getString("brandlogo");

        JSONArray ja = o.getJSONArray("lecture");
        ArrayList<Lecture> lectures=new ArrayList<>();
        for(int i=0;i<ja.length();i++){
            JSONObject oo = ja.getJSONObject(i);
            Lecture lecture = new Lecture();
            lecture.setName(oo.getString("brand_name"));
            lecture.setImage(pre + oo.getString("brand_logo"));
            lecture.setInfo(oo.getString("brand_desc"));
            lecture.setLectureId(oo.getString("brand_id"));
            lecture.setGoods_id(o.getString("id"));
            lectures.add(lecture);
        }
        course.setLectureList(lectures);

        return course;
    }

    public static Cared getCared(Context context,JSONObject o) throws  JSONException{
        Cared cared = new Cared();
        cared.setUserId(AccountUtil.getUserId(context));
        cared.setGoods_id(o.getString("id"));
        cared.setName(o.getString("name"));
        cared.setImage(o.getString("goods_thumb"));
        cared.setAuthority(o.getInt("authority"));
        cared.setCategoryName(o.getString("brief"));
        CourseHistoryUtil.addToCare(context, cared);
        List<Lecture> lectures = new ArrayList<>();
        JSONArray a = o.getJSONArray("lecture");
        for(int i=0;i<a.length();i++){
            JSONObject object = a.getJSONObject(i);
            Lecture lecture = new Lecture();
            lecture.setGoods_id(cared.getGoods_id());
            lecture.setUserId(cared.getUserId());
            lecture.setName(object.getString("brand_name"));
            lecture.setLectureId(object.getString("brand_id"));
            lecture.setImage(object.getString("brand_logo"));
            lecture.setInfo(object.getString("brand_desc"));
            CourseHistoryUtil.addLecture(context, lecture);
            lectures.add(lecture);
        }
        cared.setLectureList(lectures);
        return cared;
    }

    public static Bought getBought(Context context,JSONObject o) throws  JSONException{
        Bought cared = new Bought();
        cared.setUserId(AccountUtil.getUserId(context));
        cared.setGoods_id(o.getString("goods_id"));
        cared.setName(o.getString("goods_name"));
        cared.setImage(o.getString("goods_img"));
        System.out.println(cared.getGoods_id());
        cared.setAuthority(o.getInt("authority"));
        cared.setCategoryName(o.getString("brief"));
        CourseHistoryUtil.addToBought(context, cared);

        List<Lecture> lectures = new ArrayList<>();
        JSONArray a = o.getJSONArray("lecture");
        for(int i=0;i<a.length();i++){
            JSONObject object = a.getJSONObject(i);
            Lecture lecture = new Lecture();
            lecture.setGoods_id(cared.getGoods_id());
            lecture.setUserId(cared.getUserId());
            lecture.setName(object.getString("brand_name"));
            lecture.setLectureId(object.getString("brand_id"));
            lecture.setImage(object.getString("brand_logo"));
            lecture.setInfo(object.getString("brand_desc"));
            CourseHistoryUtil.addLecture(context, lecture);
            lectures.add(lecture);
        }
        cared.setLectureList(lectures);
        return cared;
    }
}
