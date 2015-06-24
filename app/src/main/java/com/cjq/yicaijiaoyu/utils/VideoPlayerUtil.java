package com.cjq.yicaijiaoyu.utils;

import android.content.Intent;
import android.util.Log;

import com.cjq.yicaijiaoyu.listener.CommonDownloadListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.Video;

import java.io.File;

/**
 * Created by android on 2015/6/15.
 */
public class VideoPlayerUtil {

    /**
     * 下载视频
     * @param vid 视频id
     * @param bitRate 比特率
     * @param listener 回调钩子
     */
    public static void downloadVideo(String vid,int bitRate,CommonDownloadListener listener){
        PolyvDownloader downloader = new PolyvDownloader(vid, bitRate);
        downloader.setPolyvDownloadProressListener(listener);
        //在开始下载线程之前，检查目录是否存在
        File dir = downloader.getDownloadTSDir();
        if(!dir.exists()){
            dir.mkdirs();
        }
        //todo 将downloader和下载视图绑定（为了方便暂停）
        downloader.start();
    }

    /**
     * 删除视频
     * @param vid 视频id
     * @param bitRate 比特率
     */
    public static void deleteVideo(String vid,int bitRate){
        PolyvDownloader downloader =new PolyvDownloader(vid,bitRate);
        downloader .deleteVideo(vid, bitRate);
    }

    /**
     * 删除整个下载缓存目录
     */
    public static void deleteVideoDir(){
        PolyvDownloader.cleanDownloadDir();
    }

    /*public static long getSize(int vid){
        Video video =new Video();
        Video.loadVideo(vid, new Video.OnVideoLoaded(){
            public void onloaded(Video v){
                Log.i("filesize", v.getFilesize(1) + "");


            }
        });
    }*/
}
