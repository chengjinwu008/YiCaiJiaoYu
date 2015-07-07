package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/7/6.
 */
public class TeamFragment extends Fragment implements View.OnClickListener {

    private View view;
    private int tab=1;
    private View tab1;
    private View tab2;
    private View tab3;
    private View soap;
    private View tabP1;
    private View tabP2;
    private View tabP3;
    private Handler mHandler =new Handler();
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.team_layout,container,false);

        //注册菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);

        //注册tab
        tab1 = view.findViewById(R.id.tab1);
        tab2 =view.findViewById(R.id.tab2);
        tab3=view.findViewById(R.id.tab3);
        tabP1 = view.findViewById(R.id.tabP1);
        tabP2 = view.findViewById(R.id.tabP2);
        tabP3 = view.findViewById(R.id.tabP3);
        tabP1.setOnClickListener(this);
        tabP2.setOnClickListener(this);
        tabP3.setOnClickListener(this);

        //注册soap
        soap = view.findViewById(R.id.soap);

        //注册pager
        pager = (ViewPager) view.findViewById(R.id.pager2);

        //初始化pager
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TeamIndexFragment());
        fragments.add(new TeamIndexFragment());
        fragments.add(new TeamIndexFragment());
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(),fragments);

        pager.setAdapter(adapter);

        changeFragment();
        return view;
    }

    public void changeFragment(){
        pager.setCurrentItem(tab-1);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveSoap();
            }
        },500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tabP1:
                tab=1;
                moveSoap();
                changeFragment();
                break;
            case R.id.tabP2:
                tab=2;
                moveSoap();
                changeFragment();
                break;
            case R.id.tabP3:
                tab=3;
                moveSoap();
                changeFragment();
                break;
            case R.id.main_left_menu_button:
                EventBus.getDefault().post(new MainMenuEvent());
                break;
        }
    }

    private void moveSoap() {
        switch (tab){
            case 1:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                layoutParams.leftMargin = tab1.getLeft()+tabP1.getLeft();
                layoutParams.width = tab1.getMeasuredWidth();
                soap.setLayoutParams(layoutParams);
                break;
            case 2:
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                layoutParams1.leftMargin = tab2.getLeft()+tabP2.getLeft();
                layoutParams1.width = tab2.getMeasuredWidth();
                soap.setLayoutParams(layoutParams1);
                break;
            case 3:
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                layoutParams2.leftMargin = tab3.getLeft()+tabP3.getLeft();
                layoutParams2.width = tab3.getMeasuredWidth();
                soap.setLayoutParams(layoutParams2);
                break;
        }
    }
}
