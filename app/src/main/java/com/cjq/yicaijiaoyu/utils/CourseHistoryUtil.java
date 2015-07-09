package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.dao.CourseDao;
import com.cjq.yicaijiaoyu.dao.DaoMaster;
import com.cjq.yicaijiaoyu.dao.DaoSession;
import com.cjq.yicaijiaoyu.dao.Video;
import com.cjq.yicaijiaoyu.dao.VideoDao;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.LectureEntity;
import com.cjq.yicaijiaoyu.entities.NewHistoryAddedEvent;
import com.cjq.yicaijiaoyu.entities.VideoEntity;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by CJQ on 2015/7/2.
 */
public class CourseHistoryUtil {

    public static void addToHistory(Context context,CourseEntity courseEntity){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();
        deleteToTen(courseDao);

        Course course = new Course();
        course.setCategory(courseEntity.getCategory());
        course.setTitle(courseEntity.getTitle());
        course.setCourseId(courseEntity.getId());
        course.setFree(courseEntity.isFree());
        course.setIntro(courseEntity.getIntro());
        course.setLectureName(courseEntity.getLecture().getName());
        course.setProgress(courseEntity.getProgress());
        course.setLength(courseEntity.getLength());
        course.setRequestApi(courseEntity.getRequestApi());
        course.setThumb(courseEntity.getCover_image_url());

        courseDao.insertWithoutSettingPk(course);
        db.close();

        EventBus.getDefault().post(new NewHistoryAddedEvent());
    }

    public static void deleteToTen(CourseDao courseDao){
        if(courseDao.count()>=10){
            List<Course> courses = courseDao.queryBuilder().orderAsc(CourseDao.Properties.Id).listLazy();
            courses.get(0).delete();
            deleteToTen(courseDao);
        }
    }

    public static void deleteAll(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();
        courseDao.deleteAll();
        db.close();
    }

    public static List<CourseEntity> listHistory(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();

        List<Course> courses = courseDao.queryBuilder().list();
        List<CourseEntity> res=new ArrayList<>();

        for(Course c:courses){
            CourseEntity temp = new CourseEntity();

            temp.setCategory(c.getCategory());
            temp.setTitle(c.getTitle());
            temp.setId(c.getCourseId());
            temp.setFree(c.getFree());
            temp.setIntro(c.getIntro());
            temp.setLecture(new LectureEntity(c.getLectureName(),null,null));
            temp.setProgress(c.getProgress());
            temp.setLength(c.getLength());
            temp.setRequestApi(c.getRequestApi());
            temp.setCover_image_url(c.getThumb());

            res.add(temp);
        }

        db.close();
        return res;
    }

    public static Course getOne(Context context, final String id){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();

        Course course = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append("id = ").append(id);
            }
        }).unique();
        db.close();
        return course;
    }

    public static void addVideoToCacheLog(Context context,VideoEntity videoEntity){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "video_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();

        Video v =new Video();
        v.setName(videoEntity.getName());
        v.setVid(videoEntity.getVid());
        v.setSeek(videoEntity.getSeek());

        videoDao.insertWithoutSettingPk(v);
        db.close();
    }

    public static void deleteAllVideosCacheLog(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "video_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();
        videoDao.deleteAll();
        db.close();
    }

    public static List<VideoEntity> ListVideoCache(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "video_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();

        List<Video> videos = videoDao.queryBuilder().list();
        List<VideoEntity> res=new ArrayList<>();

        for(Video v:videos){
            VideoEntity temp = new VideoEntity();

            temp.setVid(v.getVid());
            temp.setName(v.getName());
            temp.setSeek(v.getSeek());

            res.add(temp);
        }

        db.close();
        return res;
    }
}
