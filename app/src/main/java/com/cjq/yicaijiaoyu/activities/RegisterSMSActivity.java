package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.cjq.yicaijiaoyu.utils.SharedPreferenceUtil;
import com.cjq.yicaijiaoyu.utils.TimerForSeconds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/29.
 */
public class RegisterSMSActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_PHONE_NUM="num";
    private String num;
    private boolean timerOn=true;
    private Handler mHandler = new Handler();
    private TextView timer;
    private EditText verifier;
    private EditText smsPassword;
    private View registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_validate_layout);
        Intent intent = getIntent();
        num = intent.getStringExtra(EXTRA_PHONE_NUM);
        timer = (TextView) findViewById(R.id.timer);
        verifier = (EditText) findViewById(R.id.verifier);
        smsPassword = (EditText) findViewById(R.id.sms_password);
        findViewById(R.id.back).setOnClickListener(this);
        //调用短信接口
        doSendSMS(num);

        timer.setOnClickListener(this);

        registerBtn = findViewById(R.id.do_register);

        registerBtn.setOnClickListener(this);
    }

    /**
     * 短信接口调用函数
     */
    private void doSendSMS(final String num) {
        //todo 短信接口
        Log.i("SMS","开始发送短信");
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.SMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    Toast.makeText(RegisterSMSActivity.this,object.getString("msg"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegisterSMSActivity.this, R.string.hint6,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //构建短信请求实体
                Map<String,String> params = new HashMap<>();
                SMSRequestEntity.Data data = new SMSRequestEntity.Data(num);
                SMSRequestEntity requestEntity  = new SMSRequestEntity(CommonDataObject.SMS_REQUETS_CODE,data);
                params.put("opjson",CommonDataObject.GSON.toJson(requestEntity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.start();

        //todo 打开倒计时
        new TimerForSeconds(1000, 5, new TimerForSeconds.TimerListener() {
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timer.setClickable(true);
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
    protected void onDestroy() {
        this.timerOn = false;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.do_register:
                //todo 实现注册
                StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.REGISTER_URl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            Toast.makeText(RegisterSMSActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            if("0000".equals(code)){
                                //注册成功
                                String userId = jsonObject.getJSONObject("data").getString("userId");
                                //保存userId
                                SharedPreferenceUtil.getInstance(RegisterSMSActivity.this).putString(new String[]{SharedPreferenceUtil.USER_ID},new String[]{userId});
                                setResult(RESULT_OK);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //注册致命错误
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        //组建注册请求实体
                        RegisterRequestEntity.Data data = new RegisterRequestEntity.Data(num,smsPassword.getText().toString(),verifier.getText().toString());
                        RegisterRequestEntity entity = new RegisterRequestEntity(CommonDataObject.REGISTER_CODE,data);
                        params.put("opjson", CommonDataObject.GSON.toJson(entity));
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(this);

                queue.add(request);
                queue.start();
                break;
            case R.id.timer:
                doSendSMS(num);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
