package com.cjq.yicaijiaoyu.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
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
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.entities.AllCourseRequestEntity;
import com.cjq.yicaijiaoyu.utils.JsonUtil;
import com.cjq.yicaijiaoyu.utils.VideoUtil;
import com.markmao.pulltorefresh.widget.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/29.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private EditText search;
    private int nowPage=1;
    private XListView refreshView;
    private List<Course> courseEntities;
    private CourseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        search = (EditText) findViewById(R.id.search_input);
        findViewById(R.id.back).setOnClickListener(this);
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setOnEditorActionListener(this);
        refreshView = (XListView) findViewById(R.id.list);
        refreshView.setXListViewListener(this);
        refreshView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            //todo 执行搜索
            refresh();
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(search.getWindowToken(),0);
            return true;
        }
        return false;
    }

    private void refresh() {
        nowPage=1;
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if("0000".equals(object.getString("code"))){
                        if(courseEntities==null)
                            courseEntities=new ArrayList<>();
                        else
                            courseEntities.clear();
                        JSONArray a =object.getJSONObject("data").getJSONObject("categories").getJSONArray("goods");
                        for(int i=0;i<a.length();i++) {
                            JSONObject o=a.getJSONObject(i);
                            courseEntities.add(JsonUtil.getFromCourseListNoCache(SearchActivity.this,o));
                        }
                        refreshView.setAdapter(new CourseListAdapter(courseEntities,SearchActivity.this));
                        refreshView.stopRefresh();
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
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(nowPage,CommonDataObject.COURSE_NUM_SHOWING);
                data.setKeyword(search.getText().toString());
                AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SEARCH_COURSE_REQUEST_CODE,data);
                params.put("opjson",CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.start();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMore() {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if("0000".equals(object.getString("code"))){

                        if(object.getJSONObject("data").getJSONObject("categories").getJSONArray("goods").length()==0){
                            refreshView.stopLoadMore();
                            Toast.makeText(SearchActivity.this, getString(R.string.hint11), Toast.LENGTH_SHORT).show();
                        }else{
                            List<Course> temp = new ArrayList<>();
                            JSONArray a =object.getJSONObject("data").getJSONObject("categories").getJSONArray("goods");
                            for(int i=0;i<a.length();i++) {
                                JSONObject o=a.getJSONObject(i);
                                temp.add(JsonUtil.getFromCourseListNoCache(SearchActivity.this,o));
                            }
                            courseEntities.addAll(temp);

                            adapter.notifyDataSetChanged();
                            refreshView.stopLoadMore();
                        }
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
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(++nowPage,CommonDataObject.COURSE_NUM_SHOWING);
                data.setKeyword(search.getText().toString());
                AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SEARCH_COURSE_REQUEST_CODE,data);
                params.put("opjson",CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Course entity = courseEntities.get(position);
        VideoUtil.startVideo(this, entity);
    }
}
