package com.cjq.yicaijiaoyu.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.cjq.yicaijiaoyu.adapter.ChapterAdapter;
import com.cjq.yicaijiaoyu.entities.ChapterRequestEvent;
import com.cjq.yicaijiaoyu.entities.ChapterResultEvent;
import com.ypy.eventbus.EventBus;

/**
 * Created by CJQ on 2015/6/25.
 */
public class ChapterFragment extends ListFragment {

    private ChapterAdapter adapter;

    public void onEventMainThread(ChapterResultEvent e){
        getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
        getListView().invalidate();
        adapter = e.getAdapter();

        this.setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求数据

        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new ChapterRequestEvent());
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
