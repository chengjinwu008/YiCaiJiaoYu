package com.cjq.yicaijiaoyu.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.UserLoginEvent;
import com.cjq.yicaijiaoyu.utils.DialogUtil;

/**
 * Created by android on 2015/6/15.
 */
public class BaseActivity extends FragmentActivity {

    private BroadcastReceiver receiver;
    public static final String SHUTDOWN_ACTION = "com.lanhai.action.shutdown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(SHUTDOWN_ACTION.equals(intent.getAction())){
                    finish();
                }
            }
        };

        IntentFilter filter = new IntentFilter(SHUTDOWN_ACTION);

        registerReceiver(receiver,filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            DialogUtil.showExitDialog(this);
        }
        return super.onKeyUp(keyCode, event);
    }
}
