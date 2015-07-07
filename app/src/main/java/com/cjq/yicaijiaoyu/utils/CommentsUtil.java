package com.cjq.yicaijiaoyu.utils;

import android.content.Context;
import android.content.Intent;

import com.cjq.yicaijiaoyu.activities.CommentActivity;

/**
 * Created by CJQ on 2015/7/6.
 */
public class CommentsUtil {
    public static void showCommentsActivity(Context context,String goods_id,String userId){
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(CommentActivity.INTENT_GOODS_ID,goods_id);
        intent.putExtra(CommentActivity.INTENT_USER_ID,userId);
        context.startActivity(intent);
    }
}
