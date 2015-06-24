package com.cjq.yicaijiaoyu;

import com.cjq.yicaijiaoyu.listener.RequestErrListener;

/**
 * Created by android on 2015/6/15.
 */
public class CommonDataObject {
    public final static String DIR="YiCaiJiaoYu";
    public final static String POLYV_INIT_URL = "";//视频播放器初始化请求地址
    public static RequestErrListener ERROR_LISTENER=null;
    public static final int UPLOAD=0x246;
    public static int menuChecked=0;
    public static int categoryChecked=0;
}
