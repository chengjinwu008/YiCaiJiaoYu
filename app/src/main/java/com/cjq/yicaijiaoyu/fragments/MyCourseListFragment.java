package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;
import com.cjq.yicaijiaoyu.entities.CourseCategory;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.CourseListRequestEvent;
import com.cjq.yicaijiaoyu.entities.CourseListResultEvent;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.utils.VideoUtil;
import com.cjq.yicaijiaoyu.widget.XListView;
import com.ypy.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CJQ on 2015/6/29.
 */
public class MyCourseListFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private XListView view;
    private CourseListAdapter adapter;
    private String requestCode;

    public void onEventMainThread(CourseListResultEvent e){
        //请求到了数据就进行回显
        adapter =  e.getAdapter();
        view.setAdapter(adapter);
        requestCode = e.getRequestCode();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = new XListView(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        view.setPullRefreshEnable(true);
        view.setPullLoadEnable(true);
        view.setAutoLoadEnable(true);

        view.setXListViewListener(this);
        view.setRefreshTime(getTime());

        view.setOnItemClickListener(this);

        //注册事件
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CourseListRequestEvent());

        return view;
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
                if(requestCode==null){

                }else{

                }
                onLoad();
            }
            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
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
                adapter.getCourses().add(new CourseEntity("https://www.baidu.com/img/bd_logo1.png","新加的课程", "基础班",true));
                onLoad();
            }
            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击课程后，需要跳转
        CourseEntity course = adapter.getCourses().get(position-1);
        VideoUtil.startVideo(getActivity(), course);
    }

    private void onLoad() {
        view.stopRefresh();
        view.stopLoadMore();
        view.setRefreshTime(getTime());
        view.setPullRefreshEnable(true);
//        view.setPullLoadEnable(true);
//        view.setAutoLoadEnable(true);
        adapter.notifyDataSetChanged();
    }

    private void isLoading() {
        view.setPullRefreshEnable(false);
//        view.setPullLoadEnable(false);
//        view.setAutoLoadEnable(false);
    }
}
