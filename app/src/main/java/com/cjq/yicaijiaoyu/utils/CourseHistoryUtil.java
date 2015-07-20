package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cjq.yicaijiaoyu.dao.Ad;
import com.cjq.yicaijiaoyu.dao.AdDao;
import com.cjq.yicaijiaoyu.dao.Bought;
import com.cjq.yicaijiaoyu.dao.BoughtDao;
import com.cjq.yicaijiaoyu.dao.Cared;
import com.cjq.yicaijiaoyu.dao.CaredDao;
import com.cjq.yicaijiaoyu.dao.Category;
import com.cjq.yicaijiaoyu.dao.CategoryDao;
import com.cjq.yicaijiaoyu.dao.Chapter;
import com.cjq.yicaijiaoyu.dao.ChapterDao;
import com.cjq.yicaijiaoyu.dao.Comments;
import com.cjq.yicaijiaoyu.dao.CommentsDao;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.dao.CourseDao;
import com.cjq.yicaijiaoyu.dao.DaoMaster;
import com.cjq.yicaijiaoyu.dao.DaoSession;
import com.cjq.yicaijiaoyu.dao.History;
import com.cjq.yicaijiaoyu.dao.HistoryDao;
import com.cjq.yicaijiaoyu.dao.Lecture;
import com.cjq.yicaijiaoyu.dao.LectureDao;
import com.cjq.yicaijiaoyu.dao.Video;
import com.cjq.yicaijiaoyu.dao.VideoDao;
import com.cjq.yicaijiaoyu.entities.NewHistoryAddedEvent;
import com.ypy.eventbus.EventBus;

import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by CJQ on 2015/7/2.
 */
public class CourseHistoryUtil {

    public static void addToHistory(final Context context, final Course course) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        HistoryDao historyDao = daoSession.getHistoryDao();
        deleteToTen(historyDao, context);
        History history = historyDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(HistoryDao.Properties.Goods_id.columnName).append("=").append(course.getGoods_id())
                        .append(" and ").append(HistoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        boolean flagNew = false;
        if (history == null) {
            history = new History();
            flagNew = true;
        }
        history.setName(course.getName());
        history.setAuthority(course.getAuthority());
        history.setBought(course.getBought());
        history.setUserId(course.getUserId());
        history.setCared(course.getCared());
        history.setGoods_id(course.getGoods_id());
        history.setImage(course.getImage());
        history.setUserId(AccountUtil.getUserId(context));
        if (!flagNew) {
            history.update();
            historyDao.update(history);
        } else {
            historyDao.insert(history);
        }
        helper.close();

        //todo 广播新的课程添加事件
        EventBus.getDefault().post(new NewHistoryAddedEvent());
    }

    public static void addToBought(final Context context, final Bought course) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        BoughtDao boughtDao = daoSession.getBoughtDao();
        Bought bought = boughtDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(BoughtDao.Properties.Goods_id.columnName).append("=").append(course.getGoods_id())
                        .append(" and ").append(BoughtDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        boolean flagNew = false;
        if (bought == null) {
            bought = new Bought();
            flagNew = true;
        }
        bought.setName(course.getName());
        bought.setAuthority(course.getAuthority());
        bought.setUserId(course.getUserId());
        bought.setCared(course.getCared());
        bought.setGoods_id(course.getGoods_id());
        bought.setImage(course.getImage());
        bought.setUserId(AccountUtil.getUserId(context));
        if (!flagNew) {
            bought.update();
            boughtDao.update(bought);
        } else {
            boughtDao.insert(bought);
        }
        helper.close();
    }

    public static void addToCare(final Context context, final Cared course) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CaredDao caredDao = daoSession.getCaredDao();
        Cared cared = caredDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CaredDao.Properties.Goods_id.columnName).append("=").append(course.getGoods_id())
                        .append(" and ").append(CaredDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        boolean flagNew = false;
        if (cared == null) {
            cared = new Cared();
            flagNew = true;
        }
        cared.setName(course.getName());
        cared.setAuthority(course.getAuthority());
        cared.setBought(course.getBought());
        cared.setUserId(course.getUserId());
        cared.setGoods_id(course.getGoods_id());
        cared.setImage(course.getImage());
        cared.setUserId(AccountUtil.getUserId(context));
        if (!flagNew) {
            cared.update();
            caredDao.update(cared);
        } else {
            caredDao.insert(cared);
        }
        helper.close();
    }

    public static void addToCourseList(final Context context, final Course course) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();
        Course course1 = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CourseDao.Properties.Goods_id.columnName).append("=").append(course.getGoods_id())
                        .append(" and ").append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        boolean flagNew = false;
        if (course1 == null) {
            flagNew = true;
        }
        course.setUserId(AccountUtil.getUserId(context));
        if (!flagNew) {
            course.setId(course1.getId());
            courseDao.update(course);
        } else {
            courseDao.insert(course);
        }

    }

    public static void addChapter(final Context context, final Chapter chapter) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        ChapterDao chapterDao = daoSession.getChapterDao();
        Chapter chapter1 = chapterDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(ChapterDao.Properties.Chapter_id.columnName).append("=").append(chapter.getChapter_id()).append(" and ").append(ChapterDao.Properties.Goods_id.columnName).append("=").append(chapter.getGoods_id())
                        .append(" and ").append(ChapterDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        chapter.setUserId(AccountUtil.getUserId(context));
        if (chapter1 == null) {
            chapterDao.insert(chapter);
        } else {
            chapter.setId(chapter1.getId());
            chapterDao.update(chapter);
        }

    }

    public static void addVideo(final Context context, final Video video) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();

        Video video1 = videoDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(VideoDao.Properties.Chapter_id.columnName).append("=").append(video.getChapter_id()).append(" and ")
                        .append(VideoDao.Properties.Goods_id.columnName).append("=").append(video.getGoods_id())
                        .append(" and ").append(VideoDao.Properties.Vid.columnName).append("=").append(video.getVid())
                        .append(" and ").append(VideoDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        video.setUserId(AccountUtil.getUserId(context));
        if (video1 == null) {
            videoDao.insert(video);
        } else {
            video.setId(video1.getId());
            videoDao.update(video);
        }

    }

    public static void addComments(final Context context, final Comments comments) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CommentsDao commentsDao = daoSession.getCommentsDao();

        Comments comments1 = commentsDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CommentsDao.Properties.Goods_id.columnName).append("=").append(comments.getGoods_id()).append(" and ").append(CommentsDao.Properties.Comments_id.columnName).append("=").append(comments.getComments_id())
                        .append(" and ").append(CommentsDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();

        comments.setUserId(AccountUtil.getUserId(context));

        if (comments1 == null) {
            commentsDao.insert(comments);
        } else {
            comments.setId(comments1.getId());
            commentsDao.update(comments);
        }
        deleteToTwenty(commentsDao,context);
        helper.close();
    }

    public static void addCategory(final Context context, final Category category) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CategoryDao categoryDao = daoSession.getCategoryDao();

        Category category1 = categoryDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CategoryDao.Properties.CategoryId.columnName).append("=").append(category.getCategoryId()).append(" and ").append(CategoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();

        category.setUserId(AccountUtil.getUserId(context));

        if (category1 == null) {
            categoryDao.insert(category);
        } else {
            category.setId(category1.getId());
            categoryDao.update(category);
        }

    }

    public static void addLecture(final Context context, final Lecture lecture) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        LectureDao lectureDao = daoSession.getLectureDao();

        Lecture lecture1 = lectureDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(LectureDao.Properties.LectureId.columnName).append("=").append(lecture.getLectureId()).append(" and ").append(LectureDao.Properties.Goods_id.columnName).append("=").append(lecture.getGoods_id())
                .append(" and ").append(LectureDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();

        lecture.setUserId(AccountUtil.getUserId(context));

        if (lecture1 == null) {
            lectureDao.insert(lecture);
        } else {
            lecture.setId(lecture1.getId());
            lectureDao.update(lecture);
        }
        helper.close();
    }

    public static void addAd(final Context context, final Ad ad) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        AdDao adDao = daoSession.getAdDao();

        Ad ad1 = adDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(AdDao.Properties.Ad_id.columnName).append("=").append(ad.getAd_id())
                        .append(" and ").append(AdDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();

        ad.setUserId(AccountUtil.getUserId(context));

        if (ad1 == null) {
            adDao.insert(ad);
        } else {
            ad.setId(ad1.getId());
            adDao.update(ad);
        }
        helper.close();
    }

    public static void historyDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        HistoryDao courseDao = daoSession.getHistoryDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(HistoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public static void adDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        AdDao adDao = daoSession.getAdDao();
        adDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(AdDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();
        helper.close();
    }

    public static void caredDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CaredDao courseDao = daoSession.getCaredDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CaredDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();
        helper.close();
    }

    public static void courseDeleteAll(final Context context,String freeString) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();
        switch (freeString) {
            case "":
                courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
                    }
                }).buildDelete().executeDeleteWithoutDetachingEntities();
                break;
            case "0":
                courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context)).append(" and ")
                                .append(CourseDao.Properties.Authority.columnName).append("=").append(2);
                    }
                }).buildDelete().executeDeleteWithoutDetachingEntities();
                break;
            case "1":
                courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context)).append(" and ")
                                .append(CourseDao.Properties.Authority.columnName).append("<>").append(2);
                    }
                }).buildDelete().executeDeleteWithoutDetachingEntities();
                break;
        }
        helper.close();
    }

    public static void courseDeleteAllByCategory(final Context context, final long categoryId, String freeString) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        final CourseDao courseDao = daoSession.getCourseDao();

        switch (freeString) {
            case "":
                courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context)).append(" and ").append(CourseDao.Properties.CategoryId.columnName).append("=").append(categoryId);
                    }
                }).buildDelete().executeDeleteWithoutDetachingEntities();
                break;
            case "0":
                courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context)).append(" and ").append(CourseDao.Properties.CategoryId.columnName).append("=").append(categoryId).append(" and ")
                                .append(CourseDao.Properties.Authority.columnName).append("=").append(2);
                    }
                }).buildDelete().executeDeleteWithoutDetachingEntities();
                break;
            case "1":
                courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context)).append(" and ").append(CourseDao.Properties.CategoryId.columnName).append("=").append(categoryId).append(" and ")
                                .append(CourseDao.Properties.Authority.columnName).append("<>").append(2);
                    }
                }).buildDelete().executeDeleteWithoutDetachingEntities();
                break;
        }
    }

    public static void boughtDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        BoughtDao boughtDao = daoSession.getBoughtDao();
        boughtDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(BoughtDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();
        helper.close();
    }

    public static void chapterDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        ChapterDao courseDao = daoSession.getChapterDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(ChapterDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public static void videoDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        VideoDao courseDao = daoSession.getVideoDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(VideoDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public static void commentsDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CommentsDao courseDao = daoSession.getCommentsDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CommentsDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public static void categoryDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CategoryDao courseDao = daoSession.getCategoryDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CategoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();
        helper.close();
    }

    public static void lectureDeleteAll(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        LectureDao courseDao = daoSession.getLectureDao();
        courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(LectureDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public static void videoDeleteOne(final Context context, Video video) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        VideoDao videoDao = daoSession.getVideoDao();

        videoDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(VideoDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).buildDelete().executeDeleteWithoutDetachingEntities();

    }

    public static List<History> listHistory(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        HistoryDao historyDao = daoSession.getHistoryDao();
        List<History> histories = historyDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(HistoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).orderDesc(HistoryDao.Properties.Id).list();
        helper.close();
        return histories;
    }

    public static List<Ad> listAd(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        AdDao adDao = daoSession.getAdDao();

        List<Ad> adList = adDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(AdDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).orderAsc(AdDao.Properties.Id).list();
        helper.close();
        return adList;
    }

    public static List<Course> listCourses(final Context context, int offset, int length, String freeString) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();

        List<Course> courses = null;
        switch (freeString) {
            case "":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
            case "1":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context))
                                .append(" and ").append(CourseDao.Properties.Authority.columnName).append("<>").append(2)
                        ;
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
            case "0":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context))
                                .append(" and ").append(CourseDao.Properties.Authority.columnName).append("=").append(2)
                        ;
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
        }
        if(courses!=null)
        for(Course c:courses){
            c.getLectureList();
        }

        helper.close();
        return courses;
    }

    //课程分类查询
    public static List<Course> listCourseByCategory(final Context context, int offset, int length, final Category category, String freeString) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();

        List<Course> courses = null;
        switch (freeString) {
            case "":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.CategoryId.columnName).append("=").append(category.getCategoryId()).append(" and ").append(CourseDao.Properties.UserId).append("=").append(AccountUtil.getUserId(context));
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
            case "1":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.CategoryId.columnName).append("=").append(category.getCategoryId()).append(" and ").append(CourseDao.Properties.UserId).append("=").append(AccountUtil.getUserId(context))
                                .append(" and ").append(CourseDao.Properties.Authority.columnName).append("<>").append(2)
                        ;
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
            case "0":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.CategoryId.columnName).append("=").append(category.getCategoryId()).append(" and ").append(CourseDao.Properties.UserId).append("=").append(AccountUtil.getUserId(context))
                                .append(" and ").append(CourseDao.Properties.Authority.columnName).append("=").append(2)
                        ;
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
        }
        return courses;
    }

    public static List<Course> listCourseByCategory(final Context context, int offset, int length, final long categoryId, String freeString) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CourseDao courseDao = daoSession.getCourseDao();

        List<Course> courses = null;
        switch (freeString) {
            case "":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.CategoryId.columnName).append("=").append(categoryId).append(" and ").append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
            case "1":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.CategoryId.columnName).append("=").append(categoryId).append(" and ").append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context))
                                .append(" and ").append(CourseDao.Properties.Authority.columnName).append("<>").append(2)
                        ;
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
            case "0":
                courses = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                    @Override
                    public void appendTo(StringBuilder stringBuilder, String s) {
                        stringBuilder.append(CourseDao.Properties.CategoryId.columnName).append("=").append(categoryId).append(" and ").append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context))
                                .append(" and ").append(CourseDao.Properties.Authority.columnName).append("=").append(2)
                        ;
                    }
                }).orderDesc(CourseDao.Properties.Id).offset(offset).limit(length).list();
                break;
        }
        helper.close();
        return courses;
    }

    public static List<Cared> listCared(final Context context, int offset, int length) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CaredDao caredDao = daoSession.getCaredDao();

        List<Cared> careds = caredDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CaredDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).orderDesc(CaredDao.Properties.Id).offset(offset).limit(length).list();

        for(Cared c:careds){
            c.getLectureList();
        }
        helper.close();
        return careds;
    }

    public static List<Bought> listBought(final Context context, int offset, int length) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        BoughtDao boughtDao = daoSession.getBoughtDao();

        List<Bought> boughts = boughtDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(BoughtDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).orderDesc(BoughtDao.Properties.Id).offset(offset).limit(length).list();

        for(Bought c:boughts){
            c.getLectureList();
        }
        helper.close();
        return boughts;
    }

    public static List<Comments> listComments(final Context context, int offset, int length, final String goods_id) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        CommentsDao commentsDao = daoSession.getCommentsDao();

        List<Comments> comments = commentsDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CommentsDao.Properties.Goods_id.columnName).append("=").append(goods_id).append(" and ").append(CommentsDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).orderDesc(CommentsDao.Properties.Id).offset(offset).limit(length).list();

        helper.close();
        return comments;
    }

    public static List<Chapter> listChapters(final Context context, final String goods_id) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        ChapterDao chapterDao = daoSession.getChapterDao();

        List<Chapter> chapters = chapterDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(ChapterDao.Properties.Goods_id.columnName).append("=").append(goods_id).append(" and ").append(ChapterDao.Properties.UserId).append("=").append(AccountUtil.getUserId(context));
            }
        }).list();

        helper.close();
        return chapters;
    }

    public static Course getCource(final Context context, final long id) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        final CourseDao courseDao = daoSession.getCourseDao();

        Course course = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CourseDao.Properties.Id.columnName).append("=").append(id).append(" and ").append(" userId ").append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        helper.close();
        return course;
    }

    public static Course getCourse(final Context context, final String goods_id) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        final CourseDao courseDao = daoSession.getCourseDao();

        Course course = courseDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CourseDao.Properties.Goods_id.columnName).append("=").append(goods_id).append(" and ").append(CourseDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).unique();
        course.getLectureList();
        helper.close();
        return course;
    }

    public static List<Category> listCategory(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "course_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        final CategoryDao categoryDao = daoSession.getCategoryDao();


        List<Category> categories = categoryDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CategoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).list();
        helper.close();

        return categories;
    }

    public static void deleteToTen(HistoryDao historyDao, final Context context) {
        if (historyDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(HistoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).count() >= 10) {
            List<History> histories = historyDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                @Override
                public void appendTo(StringBuilder stringBuilder, String s) {
                    stringBuilder.append(HistoryDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
                }
            }).orderAsc(HistoryDao.Properties.Id).listLazy();
            histories.get(0).delete();
            deleteToTen(historyDao, context);
        }
    }

    public static void deleteToTwenty(CommentsDao commentsDao, final Context context) {
        if (commentsDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
            @Override
            public void appendTo(StringBuilder stringBuilder, String s) {
                stringBuilder.append(CommentsDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
            }
        }).count() >= 20) {
            List<Comments> histories = commentsDao.queryBuilder().where(new WhereCondition.AbstractCondition() {
                @Override
                public void appendTo(StringBuilder stringBuilder, String s) {
                    stringBuilder.append(CommentsDao.Properties.UserId.columnName).append("=").append(AccountUtil.getUserId(context));
                }
            }).orderAsc(CommentsDao.Properties.Id).listLazy();
            histories.get(0).delete();
            deleteToTwenty(commentsDao, context);
        }
    }
}
