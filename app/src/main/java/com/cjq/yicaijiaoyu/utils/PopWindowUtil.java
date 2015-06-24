package com.cjq.yicaijiaoyu.utils;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by CJQ on 2015/5/6.
 */
public class PopWindowUtil {

    public static void show(View viewToShow,View viewToAttach,View parentView,PopupWindow window,int AnimationStyle){
        window.setContentView(viewToShow);
        window.setAnimationStyle(AnimationStyle);
        window.setTouchable(true);
        window.setOutsideTouchable(false);
        window.setFocusable(true);
        window.setBackgroundDrawable(new ColorDrawable(0));
        if(viewToAttach==null && parentView!=null)
        window.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        else if(viewToAttach!=null)
            window.showAsDropDown(viewToAttach);
    }
}
