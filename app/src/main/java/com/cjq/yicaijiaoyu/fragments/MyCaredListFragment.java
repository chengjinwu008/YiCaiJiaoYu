package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.CaredListAdapter;
import com.cjq.yicaijiaoyu.dao.Cared;
import com.cjq.yicaijiaoyu.entities.CaredRequestEntity;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.utils.JsonUtil;
import com.cjq.yicaijiaoyu.widget.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJQ on 2015/7/17.
 */
public class MyCaredListFragment extends Fragment implements XListView.IXListViewListener {

    private XListView xListView;
    private List<Cared> careds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        xListView = new XListView(getActivity());;
        careds = new ArrayList<>();
        xListView.setAutoLoadEnable(false);
        //获取关注的缓存
        List<Cared> temp = CourseHistoryUtil.listCared(getActivity(), getOffset(), CommonDataObject.COURSE_NUM_SHOWING);
        if(temp.size()==0){
            //没有数据
            getCaredListFromNetWork();
        }else{
            careds.addAll(temp);
            xListView.setAdapter(new CaredListAdapter(careds, getActivity()));
        }
        xListView.setXListViewListener(this);
        return xListView;
    }

    private int getPage(){
        if(careds==null)
            return 0;
        return (int)Math.ceil((double) careds.size() / CommonDataObject.COURSE_NUM_SHOWING);
    }

    private int getOffset(){
        return getPage()*CommonDataObject.COURSE_NUM_SHOWING;
    }

    private void getCaredListFromNetWork(){
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object  = new JSONObject(s);
                    if("0000".equals(object.getString("code"))){
                        JSONArray a = object.getJSONObject("data").getJSONArray("categories");
                        List<Cared> temp = new ArrayList<>();
                        //清空缓存
                        CourseHistoryUtil.caredDeleteAll(getActivity());
                        for(int i=0;i<a.length();i++){
                            JSONObject o = a.getJSONObject(i);
                            Cared cared =JsonUtil.getCared(getActivity(), o);
                            temp.add(cared);
                        }
                        careds.clear();
                        careds.addAll(temp);
                        xListView.setAdapter(new CaredListAdapter(careds, getActivity()));
                        xListView.setPullRefreshEnable(true);
                        xListView.setPullLoadEnable(true);
                        xListView.stopRefresh();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                xListView.setPullRefreshEnable(true);
                xListView.setPullLoadEnable(true);
                xListView.stopRefresh();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                CaredRequestEntity requestEntity = new CaredRequestEntity(new CaredRequestEntity.Data(AccountUtil.getUserId(getActivity()),"1",String.valueOf(CommonDataObject.COURSE_NUM_SHOWING)));
                System.out.println(CommonDataObject.GSON.toJson(requestEntity));
                params.put("opjson",CommonDataObject.GSON.toJson(requestEntity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }

    @Override
    public void onRefresh() {
        //刷新
        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(false);
        getCaredListFromNetWork();
    }

    @Override
    public void onLoadMore() {
        //加载更多
        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(false);
        List<Cared> temp = CourseHistoryUtil.listCared(getActivity(), getOffset(), CommonDataObject.COURSE_NUM_SHOWING);
        if(temp.size()>0){
            careds.addAll(temp);
            xListView.stopLoadMore();
            xListView.setPullRefreshEnable(true);
            xListView.setPullLoadEnable(true);
        }else{
            StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object  = new JSONObject(s);
                        if("0000".equals(object.getString("code"))){
                            JSONArray a = object.getJSONObject("data").getJSONArray("categories");
                            List<Cared> temp = new ArrayList<>();
                            //清空缓存
//                            CourseHistoryUtil.caredDeleteAll(getActivity());
                            if(a.length()==0)
                                Toast.makeText(getActivity(), R.string.hint11, Toast.LENGTH_SHORT).show();
                            else
                            for(int i=0;i<a.length();i++){
                                JSONObject o = a.getJSONObject(i);
                                Cared cared =JsonUtil.getCared(getActivity(), o);
                                temp.add(cared);
                            }
//                            careds.clear();
                            careds.addAll(temp);
                            xListView.stopLoadMore();
                            xListView.setPullRefreshEnable(true);
                            xListView.setPullLoadEnable(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    xListView.stopLoadMore();
                    xListView.setPullRefreshEnable(true);
                    xListView.setPullLoadEnable(true);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();

                    CaredRequestEntity requestEntity = new CaredRequestEntity(new CaredRequestEntity.Data(AccountUtil.getUserId(getActivity()),String.valueOf(getPage() + 1),String.valueOf(CommonDataObject.COURSE_NUM_SHOWING)));

                    params.put("opjson",CommonDataObject.GSON.toJson(requestEntity));
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
            queue.start();
        }
    }
}
