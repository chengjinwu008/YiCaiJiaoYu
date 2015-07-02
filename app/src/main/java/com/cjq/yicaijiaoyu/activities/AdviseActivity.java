package com.cjq.yicaijiaoyu.activities;

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
import com.cjq.yicaijiaoyu.entities.AdviseRequestEntity;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CJQ on 2015/7/2.
 */
public class AdviseActivity extends BaseActivity implements View.OnClickListener {

    private EditText advise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_a_advise_layout);
        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        advise = (EditText)findViewById(R.id.advise);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                //组织意见反馈
                if(AccountUtil.isLoggedIn(this)){
                    StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.ADVISE_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject o = new JSONObject(s);
                                if("0000".equals(o.getString("code"))){
                                    //意见提交成功
                                    Toast.makeText(AdviseActivity.this,R.string.thank_your_advise,Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            AdviseRequestEntity.Data data = new AdviseRequestEntity.Data(AccountUtil.getUserId(AdviseActivity.this),advise.getText().toString(),AdviseActivity.this);
                            AdviseRequestEntity entity = new AdviseRequestEntity(CommonDataObject.ADVISE_REQUEST_CODE,data);

                            Map<String,String> params = new HashMap<>();
                            params.put("opjson",CommonDataObject.GSON.toJson(entity));
                            return params;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(AdviseActivity.this);
                    queue.add(request);
                    queue.start();
                }else{
                    DialogUtil.showLoginAlert(this);
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
