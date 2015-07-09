package com.cjq.yicaijiaoyu.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.activities.BaseActivity;
import com.easefun.polyvsdk.PolyvDownloader;

/**
 * Created by CJQ on 2015/7/1.
 */
public class DialogUtil {

    private static AlertDialog showLoginAlertDialog;
    private static AlertDialog showExitDialog;
    private static AlertDialog showPayDialog;
    private static AlertDialog netWorkDialog;
    private static AlertDialog downloadDialog;
    private static AlertDialog cleanDialog;

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
                    dialog.dismiss();
                    Intent intent = new Intent(BaseActivity.SHUTDOWN_ACTION);
                    context.sendBroadcast(intent);
                }
            }).create();
        }
       showExitDialog.show();
    }

    public static void showPayDialog(final Context context,final Runnable doWidthCancel){
        if(showPayDialog==null){
            showPayDialog = new AlertDialog.Builder(context).setMessage(R.string.buy_or_not).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doWidthCancel.run();
                }
            }).setPositiveButton(R.string.buy, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //todo 打开支付
                }
            }).create();
        }
        showPayDialog.show();
    }

    public static void showNetWorkDialog(final Context context,final Runnable doWidthCancel){
        if(netWorkDialog==null){
            netWorkDialog = new AlertDialog.Builder(context).setMessage(R.string.no_network).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doWidthCancel.run();
                }
            }).setPositiveButton(R.string.set_network, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //todo 打开网络设置
                }
            }).create();
        }
        netWorkDialog.show();
    }

    public static void showDownloadDialog(Context context,final Runnable doWithCertain){
        if(downloadDialog==null){
            downloadDialog = new AlertDialog.Builder(context).setMessage(R.string.download_hint).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doWithCertain.run();
                }
            }).create();
        }
        downloadDialog.show();
    }


    public static void showCleanDialog(Context context, final String vid){
        if(cleanDialog==null){
            cleanDialog = new AlertDialog.Builder(context).setMessage(R.string.clean_hint).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //todo 清除缓存
                    if (vid == null) {
                        PolyvDownloader.cleanDownloadDir();
                    } else {
                        PolyvDownloader downloader = new PolyvDownloader(vid, 1);
                        downloader.deleteVideo(vid);
                    }
                }
            }).create();
        }
        cleanDialog.show();
    }
}
