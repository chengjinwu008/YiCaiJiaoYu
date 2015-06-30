package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cjq.yicaijiaoyu.R;

/**
 * Created by cheng on 2015/6/28.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_table);

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.forget).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                //注册
                Intent intent =new Intent(this,RegisterActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.forget:
                //忘记密码
                Intent intent1 =new Intent(this,ForgetPasswordActivity.class);
                startActivityForResult(intent1, 0);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}
