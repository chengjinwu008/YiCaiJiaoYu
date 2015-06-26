package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.adapter.CommentsAdapter;

/**
 * Created by CJQ on 2015/6/26.
 */
public class CommentsResultEvent {

    CommentsAdapter adapter;

    public CommentsResultEvent(CommentsAdapter adapter) {
        this.adapter = adapter;
    }

    public CommentsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CommentsAdapter adapter) {
        this.adapter = adapter;
    }
}
