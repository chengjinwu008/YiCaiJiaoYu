package com.cjq.yicaijiaoyu.applications;

import android.app.Application;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.listener.RequestErrListener;
import com.easefun.polyvsdk.PolyvSDKClient;

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
        String userid = null;
        String privatekey = null;
        String writetoken = null;
        String readtoken = null;

        //todo 请求token
        //判断sd卡可读可写
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File saveDir=new File(Environment.getExternalStorageDirectory()+ File.separator+ CommonDataObject.DIR);

            StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.POLYV_INIT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //todo 处理返回的json
                }
            },CommonDataObject.ERROR_LISTENER);

            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setReadtoken(readtoken);
            client.setWritetoken(writetoken);
            client.setPrivatekey(privatekey);
            client.setUserId(userid);
            client.setSign(true);
            client.setDownloadDir(saveDir);//下载文件的目录
            client.startService(getApplicationContext());//启动服务
        }else {
            //sd卡不可读写就提示用户，并且不初始化视频服务

        }
    }
}
