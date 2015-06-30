package com.cjq.yicaijiaoyu.utils;

import android.app.Activity;
import android.content.Intent;

import com.cjq.yicaijiaoyu.activities.PlayActivity;
import com.cjq.yicaijiaoyu.entities.CourseEntity;

/**
 * Created by CJQ on 2015/6/25.
 */
public class VideoUtil {
    public static void startVideo(Activity activity,CourseEntity course){
        //首先判断是不是收费的
        if(course.isFree()){
            Intent intent = new Intent(activity, PlayActivity.class);
            intent.putExtra(PlayActivity.URL,course.getRequestApi());
            intent.putExtra(PlayActivity.TITLE,course.getTitle());
            intent.putExtra(PlayActivity.PROGRESS,course.getProgress());
            intent.putExtra(PlayActivity.LEC_NAME,course.getLecture().getName());
            intent.putExtra(PlayActivity.LEC_INTRO,course.getLecture().getIntro());
            intent.putExtra(PlayActivity.LEC_PORT,course.getLecture().getPortrait_url());
            intent.putExtra(PlayActivity.INTRO,course.getIntro());
            intent.putExtra(PlayActivity.ID,course.getId());
            activity.startActivity(intent);
        }else {
            //判断用户登录
            if(AccountUtil.isLoggedIn(activity)){
                //todo 登录状态，进一步判断用户的播放权限

            }else{
                //todo 未登录，提示警告

            }
        }
    }
}
