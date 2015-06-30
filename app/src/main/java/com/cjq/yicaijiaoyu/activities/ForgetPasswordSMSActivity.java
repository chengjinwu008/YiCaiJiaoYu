package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.RegisterRequestEntity;
import com.cjq.yicaijiaoyu.entities.SMSRequestEntity;
import com.cjq.yicaijiaoyu.utils.TimerForSeconds;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/29.
 */
public class ForgetPasswordSMSActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_PHONE_NUM="num";
    private Handler mHandler=new Handler();
    private TextView timer;
    private boolean timerOn=true;
    private String num;
    private EditText smsPasswordRe;
    private EditText verifier;
    private EditText smsPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_validate_layout);
        timer = (TextView) findViewById(R.id.timer);
        Intent intent = getIntent();
        num = intent.getStringExtra(EXTRA_PHONE_NUM);
        smsPasswordRe = (EditText)findViewById(R.id.sms_password_re);
        verifier = (EditText) findViewById(R.id.verifier);
        smsPassword = (EditText) findViewById(R.id.sms_password);

        timer.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        doSendSMS(num);
    }

    /**
     * 短信接口调用函数
     */
    private void doSendSMS(final String num) {
        //todo 短信接口
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.FORGET_SMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(ForgetPasswordSMSActivity.this, getString(R.string.hint5), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ForgetPasswordSMSActivity.this, R.string.hint6,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //构建短信请求实体
                Map<String,String> params = new HashMap<>();
                SMSRequestEntity.Data data = new SMSRequestEntity.Data(num);
                SMSRequestEntity requestEntity  = new SMSRequestEntity(CommonDataObject.FORGET_SMS_REQUEST_CODE,data);
                params.put("opjson",CommonDataObject.GSON.toJson(requestEntity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.start();

        //todo 打开倒计时
        new TimerForSeconds(1000, 60, new TimerForSeconds.TimerListener() {
            @Override
            public void onEverySeconds(final int timeLeft) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(timer.isClickable())
                            timer.setClickable(false);
                        timer.setText(timeLeft+getString(R.string.resend));
                    }
                });
            }

            @Override
            public void onTimeUp() {
                timer.setClickable(true);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timer.setText(R.string.resend_no_time);
                    }
                });
            }

            @Override
            public boolean getTimerFlag() {
                return timerOn;
            }

            @Override
            public boolean getTimerPauseFlag() {
                return false;
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timer:
                doSendSMS(num);
                break;
            case R.id.change_confirm:
                //todo 检测确认密码和密码输入是否一致
                final String password = smsPassword.getText().toString();
                String rePassword = smsPasswordRe.getText().toString();

                if(!password.equals("") && password.equals(rePassword)){
                    StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.RESET_PASSWORD_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //注册请求成功
                            setResult(RESULT_OK);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //注册请求致命错误
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //构建请求实体
                            RegisterRequestEntity.Data data = new RegisterRequestEntity.Data(num,password,verifier.getText().toString());
                            RegisterRequestEntity entity = new RegisterRequestEntity(CommonDataObject.FORGET_SMS_REQUEST_CODE,data);
                            params.put("opjson",CommonDataObject.GSON.toJson(entity));
                            return params;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(this);
                    queue.add(request);
                    queue.start();
                }else{
                    Toast.makeText(this,R.string.hint9,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
