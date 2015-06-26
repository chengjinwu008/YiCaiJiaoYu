package com.cjq.yicaijiaoyu.utils;

import com.cjq.yicaijiaoyu.CommonDataObject;

/**
 * Created by CJQ on 2015/6/26.
 */
public class NetStateUtil {
    public interface NetWorkStateListener{

        void doWithNetWork();

        void doWithoutNetWork();
    }

    public static void checkNetwork(NetWorkStateListener listener){
        if(CommonDataObject.CM!=null && CommonDataObject.CM.getActiveNetworkInfo()!=null){
            if(CommonDataObject.CM.getActiveNetworkInfo().isAvailable()){
                listener.doWithNetWork();
            }else{
                //网络不可用
                listener.doWithoutNetWork();
            }
        }else{
            //网络不可用
            listener.doWithoutNetWork();
        }
    }
}
