package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.dao.CourseDao;
import com.cjq.yicaijiaoyu.dao.DaoMaster;
import com.cjq.yicaijiaoyu.dao.DaoSession;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.LectureEntity;

import java.util.ArrayList;
import java.util.List;

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

}
