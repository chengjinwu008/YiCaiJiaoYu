package com.cjq.yicaijiaoyu.utils;

import android.app.Activity;
import android.content.Intent;

import com.cjq.yicaijiaoyu.activities.PlayActivity;
import com.cjq.yicaijiaoyu.dao.Course;

/**
 * Created by CJQ on 2015/6/25.
 */
public class VideoUtil {
    public static void startVideo(Activity activity, Course course) {
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(PlayActivity.ID, course.getGoods_id());
        activity.startActivity(intent);

        CourseHistoryUtil.addToHistory(activity,course);
    }
}
