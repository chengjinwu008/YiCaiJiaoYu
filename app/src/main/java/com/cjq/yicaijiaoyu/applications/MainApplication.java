package com.cjq.yicaijiaoyu.applications;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.listener.RequestErrListener;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * Created by android on 2015/6/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //数据初始化
        CommonDataObject.ERROR_LISTENER = new RequestErrListener();
        String userid = "ea307b2422";
        String privatekey = "Uy0nwD0QJe";
        String writetoken = "Dndk3La1mZCWa5i-fAAaTqsOJjr328Ed";
        String readtoken = "ekNNqIqQOk-IrcU0T7O5J-KPTih5x6GE";
        //判断sd卡可读可写
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File saveDir = new File(Environment.getExternalStorageDirectory() + File.separator + CommonDataObject.DIR);

            StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.POLYV_INIT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //todo 处理返回的json

                }
            }, CommonDataObject.ERROR_LISTENER);
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setReadtoken(readtoken);
            client.setWritetoken(writetoken);
            client.setPrivatekey(privatekey);
            client.setUserId(userid);
            client.setSign(true);
            client.setDownloadDir(saveDir);//下载文件的目录
            client.startService(getApplicationContext());//启动服务
        } else {
            //sd卡不可读写就提示用户，并且不初始化视频服务

        }
    }


}
