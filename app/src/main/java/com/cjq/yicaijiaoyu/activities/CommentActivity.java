package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.CommentCommitRequestEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CJQ on 2015/7/6.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener {
    public static final String INTENT_GOODS_ID = "goods_id";
    public static final String INTENT_USER_ID = "userId";
    private EditText comments;
    private String goods_id;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_comments);
        Intent intent = getIntent();
        if(intent!=null){
            goods_id = intent.getStringExtra(INTENT_GOODS_ID);
            userId = intent.getStringExtra(INTENT_USER_ID);
        }else{
            finish();
        }


        comments = (EditText) findViewById(R.id.comments_content);
        //注册返回键
        findViewById(R.id.back).setOnClickListener(this);
        //注册评论键
        findViewById(R.id.commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.commit:
                StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COMMENTS_COMMIT_REQUEST_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        // TODO: 2015/7/6  处理评论的返回值
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        CommentCommitRequestEntity.Data data = new CommentCommitRequestEntity.Data(goods_id,comments.getText().toString(),userId);
                        CommentCommitRequestEntity entity = new CommentCommitRequestEntity(CommonDataObject.COMMENTS_COMMIT_REQUEST_CODE,data);
                        Map<String,String> params = new HashMap<>();
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
}
