package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.ShowLoginEvent;
import com.cjq.yicaijiaoyu.entities.SkipLeaderPageEvent;
import com.ypy.eventbus.EventBus;

/**
 * Created by CJQ on 2015/6/30.
 */
public class WelcomeOneFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lead_layout,container,false);
        view.findViewById(R.id.background_img).setBackgroundResource(R.drawable.katong1);
        view.findViewById(R.id.btn1).setOnClickListener(this);
        view.findViewById(R.id.btn2).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                //直接登录
                EventBus.getDefault().post(new ShowLoginEvent());
                break;
            case R.id.btn2:
                //稍候再说
                EventBus.getDefault().post(new SkipLeaderPageEvent());
                break;
        }
    }
}
