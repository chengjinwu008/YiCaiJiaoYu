package com.cjq.yicaijiaoyu;

import android.net.ConnectivityManager;

import com.cjq.yicaijiaoyu.listener.RequestErrListener;
import com.google.gson.Gson;

/**
 * Created by android on 2015/6/15.
 */
public class CommonDataObject {
    public final static String DIR="YiCaiJiaoYu";
    public static final int UPLOAD=0x246;
    public static RequestErrListener ERROR_LISTENER = null;
    public static int menuChecked=0;
    public static int categoryChecked=0;//全部课程列表筛选
    public static ConnectivityManager CM = null;
    public static String nowPlayingId=null;//当前正在播放的视频的id
    public static final Gson GSON = new Gson();
    public static int COURSE_NUM_SHOWING=5;//每页显示的条数

    public final static String MAIN_URL="http://192.168.0.181/xct";
    public final static String POLYV_INIT_URL =MAIN_URL+"";//视频播放器初始化请求地址 请求json的地方
    public static final String SMS_URL= MAIN_URL+"/service/app.php";
    public static final String REGISTER_URl=MAIN_URL+"/service/app.php";
    public static final String FORGET_SMS_URL = MAIN_URL+"/service/app.php";//忘记密码短信验证码请求接口
    public static final String RESET_PASSWORD_URL =MAIN_URL+ "/service/app.php";//重置密码请求接口
    public static final String LOGIN_URL=MAIN_URL+"/service/app.php";//用户登录接口
    public static final String USER_INFO_URL =MAIN_URL+"/service/app.php";//用户信息请求接口

    public static final String COURSE_LIST_URL = MAIN_URL+"/service/app.php";//课程列表接口

    public static final String REGISTER_CODE = "act_register"; //注册接口请求码
    public static final String FORGET_SMS_REQUEST_CODE = "act_upverifycode";//忘记密码短信请求码
    public static final String RESET_PASSWORD_REQUEST_CODE = "act_reset";//重置密码请求码
    public static final String SMS_REQUETS_CODE ="act_verifycode"; //短信验证码请求码
    public static final String COURSE_BOUGHT_REQUEST_CODE ="act_coursesearch";//已购课程请求码
    public static final String COURSE_CARE_REQUEST_CODE ="act_addcollect";//已关注课程请求码
    public static final String LOGIN_REQUEST_CODE="act_login";//登录请求码
    public static final String USER_INFO_REQUEST_CODE = "act_userinfo";//用户信息请求码
    public static final String ALLCOURSE_REQUEST_CODE ="act_coursepage";//所有课程请求码

}
