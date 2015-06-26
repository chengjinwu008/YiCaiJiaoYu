package com.cjq.yicaijiaoyu.entities;

import com.cjq.yicaijiaoyu.adapter.ChapterAdapter;

/**
 * Created by CJQ on 2015/6/26.
 */
public class ChapterResultEvent {
    ChapterAdapter adapter;

    public ChapterResultEvent(ChapterAdapter adapter) {
        this.adapter = adapter;
    }

    public ChapterAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ChapterAdapter adapter) {
        this.adapter = adapter;
    }
}
