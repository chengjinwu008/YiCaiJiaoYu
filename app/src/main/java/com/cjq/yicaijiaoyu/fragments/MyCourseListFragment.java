package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;
import com.cjq.yicaijiaoyu.entities.AllCourseRequestEntity;
import com.cjq.yicaijiaoyu.entities.CourseCategory;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.CourseListRequestEvent;
import com.cjq.yicaijiaoyu.entities.CourseListResultEvent;
import com.cjq.yicaijiaoyu.entities.NewHistoryAddedEvent;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.utils.CourseUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.utils.VideoUtil;
import com.cjq.yicaijiaoyu.widget.XListView;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/29.
 */
public class MyCourseListFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView view;
    private CourseListAdapter adapter;
    private String requestCode;
    private int NO;
    private int nowPage = 1;

    public MyCourseListFragment setNO(int NO) {
        this.NO = NO;
        return this;
    }

    public void onEventMainThread(NewHistoryAddedEvent e) {
        //播放历史更新
        if (NO == 2)
            refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = new XListView(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        view.setPullRefreshEnable(true);
//        view.setPullLoadEnable(true);
//        view.setAutoLoadEnable(true);

        view.setXListViewListener(this);
        view.setRefreshTime(getTime());

        view.setOnItemClickListener(this);

        //注册事件
        EventBus.getDefault().register(this);
        getContent();
        return view;
    }

    public void getContent() {
        if(getActivity()!=null)
        switch (NO) {
            case 0:
                sendCare(CommonDataObject.COURSE_CARE_REQUEST_CODE);
                requestCode=CommonDataObject.COURSE_CARE_REQUEST_CODE;
                break;
            case 1:
                sendCare(CommonDataObject.COURSE_BOUGHT_REQUEST_CODE);
                requestCode=CommonDataObject.COURSE_BOUGHT_REQUEST_CODE;
                break;
            case 2:
                //查询播放历史
                sendHistory();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        //todo 刷新做的事情
        isLoading();
        NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
            @Override
            public void doWithNetWork() {
                //todo 请求刷新
                if (requestCode == null) {
                    //证明是刷新播放历史
                    adapter.setCourses(CourseHistoryUtil.listHistory(getActivity()));
                } else {
                    //不是刷新历史，所以需要亲求
                    refresh();
                }
                onLoad();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
    }

    private void refresh() {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        List<CourseEntity> temp = new ArrayList<>();
                        CourseUtil.chargeCourseList(object.getJSONObject("data").getJSONArray("categories"), temp);
                        adapter.setCourses(temp);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(1, CommonDataObject.COURSE_NUM_SHOWING);
                data.setUserId(AccountUtil.getUserId(getActivity()));
                AllCourseRequestEntity entity = new AllCourseRequestEntity(requestCode, data);
                Map<String, String> params = new HashMap<>();
                params.put("opjson", CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }


    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onLoadMore() {
        //todo 加载更多做的事情
        isLoading();
        NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
            @Override
            public void doWithNetWork() {
                //todo 请求加载
                if (requestCode == null) {
                    //历史直接不给加载
                    onLoad();
                    Toast.makeText(getActivity(), getActivity().getString(R.string.hint11), Toast.LENGTH_SHORT).show();
                } else {
                    //其他的可以加载
                    load();
                }
            }

            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
    }

    private void load() {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        List<CourseEntity> temp = new ArrayList<>();
                        if (object.getJSONObject("data").getJSONArray("categories").length() == 0) {
                            //没有更多
                            nowPage--;
                            Toast.makeText(getActivity(), getActivity().getString(R.string.hint11), Toast.LENGTH_SHORT).show();
                        } else {
                            CourseUtil.chargeCourseList(object.getJSONObject("data").getJSONArray("categories"), temp);
                            adapter.getCourses().addAll(temp);
                        }
                    }
                    onLoad();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(++nowPage, CommonDataObject.COURSE_NUM_SHOWING);
                data.setUserId(AccountUtil.getUserId(getActivity()));
                AllCourseRequestEntity entity = new AllCourseRequestEntity(requestCode, data);
                Map<String, String> params = new HashMap<>();
                params.put("opjson", CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击课程后，需要跳转
        CourseEntity course = adapter.getCourses().get(position - 1);
        VideoUtil.startVideo(getActivity(), course);
    }

    private void onLoad() {
        view.stopRefresh();
        view.stopLoadMore();
        view.setRefreshTime(getTime());
        view.setPullRefreshEnable(true);
//        view.setPullLoadEnable(true);
//        view.setAutoLoadEnable(true);
    }

    private void isLoading() {
        view.setPullRefreshEnable(false);
//        view.setPullLoadEnable(false);
//        view.setAutoLoadEnable(false);
    }

    private void sendCare(String courseCareRequestCode) {
        AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(1, CommonDataObject.COURSE_NUM_SHOWING);
        data.setUserId(AccountUtil.getUserId(getActivity()));
        AllCourseRequestEntity entity = new AllCourseRequestEntity(courseCareRequestCode, data);
        getCourseListAdapterEvent(entity);
    }

    private void sendHistory() {
        List<CourseEntity> temp = CourseHistoryUtil.listHistory(getActivity());
        Log.e("TAG", String.valueOf(temp.size()));
        view.setAdapter(new CourseListAdapter(temp, getActivity()));
        view.invalidate();
    }

    private void getCourseListAdapterEvent(final AllCourseRequestEntity entity) {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        List<CourseEntity> temp = new ArrayList<>();
                        CourseUtil.chargeCourseList(object.getJSONObject("data").getJSONArray("categories"), temp);
                        adapter = new CourseListAdapter(temp, getActivity());
                        view.setAdapter(adapter);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opjson", CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }
}
