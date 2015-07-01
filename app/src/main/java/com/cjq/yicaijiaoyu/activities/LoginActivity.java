package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.cjq.yicaijiaoyu.entities.LoginRequestEntity;
import com.cjq.yicaijiaoyu.entities.UserLoginEvent;
import com.cjq.yicaijiaoyu.utils.SharedPreferenceUtil;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheng on 2015/6/28.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_table);

        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.login_button).setOnClickListener(this);

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
                startActivityForResult(intent1, 1);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.login_button:
                //实现登录
                final String userName = this.userName.getText().toString();
                final String password = this.password.getText().toString();

                StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.getString("code");
                            Toast.makeText(LoginActivity.this,object.getString("msg"),Toast.LENGTH_SHORT).show();
                            if("0000".equals(code)){
                                String userId =object.getJSONObject("data").getString("userId");
                                SharedPreferenceUtil.getInstance(LoginActivity.this).putString(new String[]{SharedPreferenceUtil.USER_ID},new String[]{userId});
                                //登录成功了
                                EventBus.getDefault().post(new UserLoginEvent());
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        LoginRequestEntity.Data data = new LoginRequestEntity.Data(userName,userName,password);
                        LoginRequestEntity entity = new LoginRequestEntity(CommonDataObject.LOGIN_REQUEST_CODE,data);
                        params.put("opjson",CommonDataObject.GSON.toJson(entity));
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(request);
                queue.start();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                //注册
                if(resultCode==RESULT_OK){
                    EventBus.getDefault().post(new UserLoginEvent());
                    finish();
                }
                break;
            case 1:
                //修改密码
                if(resultCode==RESULT_OK){

                }
                break;
        }
    }
}
