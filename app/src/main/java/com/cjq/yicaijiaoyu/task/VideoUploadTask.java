package com.cjq.yicaijiaoyu.task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.SDKUtil;
import com.easefun.polyvsdk.Video;
import com.easefun.polyvsdk.net.Progress;

import org.json.JSONException;

import java.io.File;

/**
 * 异步上传视频类
 * Created by android on 2015/6/16.
 */
public class VideoUploadTask extends AsyncTask<String, Void, String> {
    private Handler handler;

    public VideoUploadTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        File path = new File(Environment.getExternalStorageDirectory(),"myRecording.mp4");
        try {
            String videojson = PolyvSDKClient.getInstance()
                    .resumableUpload(path.toString(), "我的标题", "desc", 0,
                            new Progress() {
                                @Override
                                public void run(long offset, long max) {
                                    Message msg = new Message();
                                    msg.what = CommonDataObject.UPLOAD;
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("offset", offset);
                                    bundle.putLong("max", max);
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                }
                            });
            return videojson;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            Video video = SDKUtil.convertJsonToVideo(result);
            Log.d("VideoUploadTask", "video uploaded vid: " + video.getVid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}