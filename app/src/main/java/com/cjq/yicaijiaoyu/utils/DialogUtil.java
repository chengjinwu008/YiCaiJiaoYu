package com.cjq.yicaijiaoyu.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cjq.yicaijiaoyu.R;

/**
 * Created by CJQ on 2015/7/1.
 */
public class DialogUtil {

    private static AlertDialog showLoginAlertDialog;

    public static void showLoginAlert(final Context context){
        if(showLoginAlertDialog==null)
        showLoginAlertDialog = new AlertDialog.Builder(context).setMessage(R.string.not_login).setPositiveButton(R.string.now_login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccountUtil.showLoginActivity(context);
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.maybe_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        showLoginAlertDialog.show();
    }

}
