package com.cjq.yicaijiaoyu.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;

/**
 * Created by CJQ on 2015/6/29.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        search = (EditText) findViewById(R.id.search_input);
        findViewById(R.id.back).setOnClickListener(this);
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setOnEditorActionListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            //todo 执行搜索

            return true;
        }
        return false;
    }
}
