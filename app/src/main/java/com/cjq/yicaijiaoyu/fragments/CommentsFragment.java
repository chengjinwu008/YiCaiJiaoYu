package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.adapter.CommentsAdapter;
import com.cjq.yicaijiaoyu.dao.Comments;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.entities.CommentsEntity;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEntity;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEvent;
import com.cjq.yicaijiaoyu.entities.CommentsResultEvent;
import com.cjq.yicaijiaoyu.entities.UserEntity;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.view.AllListView;
import com.cjq.yicaijiaoyu.widget.XListView;
import com.ypy.eventbus.EventBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/25.
 */
public class CommentsFragment extends Fragment {
    private int page = 1;
    private String gid;
    private String userId;
    private AllListView view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new AllListView(getActivity(), null);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CommentsRequestEvent());
        return view;
    }

    public void onEventMainThread(CommentsResultEvent e) {
        Course course = e.getCourse();
        final List<Comments> commentsList = CourseHistoryUtil.listComments(getActivity(), 0, 20, course.getGoods_id());
        if (commentsList.size() > 0)
            view.setAdapter(new CommentsAdapter(commentsList, getActivity()));
        else {
            //请求网络进行加载
            final CommentsRequestEntity requestEntity = new CommentsRequestEntity(CommonDataObject.COMMENTS_REQUEST_CODE,new CommentsRequestEntity.Data(0,course.getGoods_id(),course.getUserId()));
            StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COMMENTS_REQUEST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    System.out.println(s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("opjson",CommonDataObject.GSON.toJson(requestEntity));
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
            queue.start();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
