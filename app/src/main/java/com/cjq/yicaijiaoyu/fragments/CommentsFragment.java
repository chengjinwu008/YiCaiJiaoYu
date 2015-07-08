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
import com.cjq.yicaijiaoyu.entities.CommentsEntity;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEntity;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEvent;
import com.cjq.yicaijiaoyu.entities.CommentsResultEvent;
import com.cjq.yicaijiaoyu.entities.UserEntity;
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
public class CommentsFragment extends Fragment implements XListView.IXListViewListener {
    CommentsAdapter adapter;
    private XListView view;
    private int page=1;
    private String gid;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = new XListView(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CommentsRequestEvent());

        view.setPullRefreshEnable(true);
        view.setXListViewListener(this);
        return view;
    }

    public void onEventMainThread(CommentsResultEvent e) {
        gid = e.getGid();
        userId = e.getUserId();
        adapter = e.getAdapter();
        view.setAdapter(adapter);
        view.stopRefresh();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(new CommentsRequestEvent());
    }

    @Override
    public void onLoadMore() {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COMMENTS_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        JSONArray comments = object.getJSONObject("data").getJSONObject("data").getJSONArray("data");
                        List<CommentsEntity> commentsEntities = new ArrayList<>();
                        for (int i = 0; i < comments.length(); i++) {
                            JSONObject c = comments.getJSONObject(i);
                            CommentsEntity cc = new CommentsEntity(c.getLong("add_time"), new UserEntity(null, c.getString("user_name"), c.getString("user_id")), c.getString("content"));
                            commentsEntities.add(cc);
                        }
                        adapter.getCommentsEntities().addAll(commentsEntities);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                CommentsRequestEntity.Data data =new CommentsRequestEntity.Data(++page,gid,userId);
                CommentsRequestEntity entity = new CommentsRequestEntity(CommonDataObject.COMMENTS_REQUEST_CODE,data);

                params.put("opjson",CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }
}
