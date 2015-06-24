package com.cjq.yicaijiaoyu.listener;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by android on 2015/6/15.
 */
public class RequestErrListener implements Response.ErrorListener {
    public static final String TAG = "RequestErrListener";

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        //请求错误的处理方法
        Log.e(TAG, String.valueOf(volleyError.networkResponse.statusCode));
    }
}
