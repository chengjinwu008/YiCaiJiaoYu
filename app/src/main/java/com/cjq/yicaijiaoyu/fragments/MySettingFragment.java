package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.SettingListAdapter;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.entities.SettingEntity;
import com.cjq.yicaijiaoyu.utils.VersionUtil;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_setting,container,false);
        //菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);
        //设置项
        ListView setting_list = (ListView) view.findViewById(R.id.setting_items);

        //意见反馈选项
        SettingEntity advises = new SettingEntity(getActivity().getString(R.string.advise),null,true);
        //清理缓存选项
        SettingEntity clean = new SettingEntity(getActivity().getString(R.string.clean),"10M",false);
        //当前版本选项
        SettingEntity version=new SettingEntity("当前版本", VersionUtil.getVersion(getActivity().getPackageManager(),getActivity().getPackageName()),false);

        List<SettingEntity> settings = new ArrayList<>();
        settings.add(advises);
        settings.add(clean);
        settings.add(version);

        setting_list.setAdapter(new SettingListAdapter(settings,getActivity()));

        setting_list.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_menu_button:
                EventBus.getDefault().post(new MainMenuEvent());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                //todo 打开意见反馈留言板

                break;
        }
    }
}
