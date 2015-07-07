package com.cjq.yicaijiaoyu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by CJQ on 2015/7/7.
 */
public class DashView  extends View{


    public DashView(Context context) {
        this(context,null);
    }

    public DashView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);
    }
}
