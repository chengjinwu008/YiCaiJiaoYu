package com.cjq.yicaijiaoyu.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.activities.BaseActivity;

/**
 * Created by CJQ on 2015/7/1.
 */
public class DialogUtil {

    private static AlertDialog showLoginAlertDialog;
    private static AlertDialog showExitDialog;
    private static AlertDialog showPayDialog;

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

    public static void showExitDialog(final Context context){
        if(showExitDialog==null){
            showExitDialog = new AlertDialog.Builder(context).setMessage(R.string.exit_or_not).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(BaseActivity.SHUTDOWN_ACTION);
                    context.sendBroadcast(intent);
                }
            }).create();
        }
       showExitDialog.show();
    }

    public static void showPayDialog(final Context context){
        if(showPayDialog==null){
            showPayDialog = new AlertDialog.Builder(context).setMessage(R.string.buy_or_not).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.buy, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //todo 打开支付
                }
            }).create();
        }
        showPayDialog.show();
    }

}
