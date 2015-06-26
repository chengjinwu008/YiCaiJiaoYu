package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.cjq.yicaijiaoyu.adapter.CommentsAdapter;
import com.cjq.yicaijiaoyu.entities.CommentsRequestEvent;
import com.cjq.yicaijiaoyu.entities.CommentsResultEvent;
import com.ypy.eventbus.EventBus;

/**
 * Created by CJQ on 2015/6/25.
 */
public class CommentsFragment extends ListFragment {
    CommentsAdapter adapter;


    public void onEventMainThread(CommentsResultEvent e){
        adapter = e.getAdapter();

        this.setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求评论数据
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CommentsRequestEvent());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
