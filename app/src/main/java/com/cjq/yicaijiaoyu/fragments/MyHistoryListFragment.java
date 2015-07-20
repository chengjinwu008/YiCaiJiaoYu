package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjq.yicaijiaoyu.adapter.HistoryListAdapter;
import com.cjq.yicaijiaoyu.dao.History;
import com.cjq.yicaijiaoyu.entities.NewHistoryAddedEvent;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.view.AllListView;
import com.ypy.eventbus.EventBus;

import java.util.List;

/**
 * Created by CJQ on 2015/7/17.
 */
public class MyHistoryListFragment extends Fragment {

    private AllListView listView;

    public void onEventMainThread(NewHistoryAddedEvent e){
        List<History> histories = CourseHistoryUtil.listHistory(getActivity());
        listView.setAdapter(new HistoryListAdapter(histories,getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new AllListView(getActivity(),null);

        //读取历史缓存
        List<History> histories = CourseHistoryUtil.listHistory(getActivity());
        listView.setAdapter(new HistoryListAdapter(histories,getActivity()));

        //注册历史变更时间
        EventBus.getDefault().register(this);
        return listView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
