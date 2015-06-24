package com.cjq.yicaijiaoyu.listener;

import com.easefun.polyvsdk.PolyvDownloadProgressListener;

/**
 * Created by android on 2015/6/15.
 */
public abstract class CommonDownloadListener implements PolyvDownloadProgressListener {

    /**
     * 正在下载的时候会调用的函数
     * @param l 当前下载了的长度
     * @param l1 总长度
     */
    @Override
    public void onDownload(long l, long l1) {

    }

    /**
     * 下载成功回调
     */
    @Override
    public void onDownloadSuccess() {

    }

    /**
     * 下载失败回调
     * @param s 错误信息
     */
    @Override
    public void onDownloadFail(String s) {

    }
}
