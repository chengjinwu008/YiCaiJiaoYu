package com.cjq.yicaijiaoyu.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by CJQ on 2015/7/1.
 */
public class AlertLoginDialog extends AlertDialog {
    public AlertLoginDialog(Context context) {
        super(context);
    }

    protected AlertLoginDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //自定义view
    }
}
