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
import com.cjq.yicaijiaoyu.entities.UserLoginEvent;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.SharedPreferenceUtil;
import com.cjq.yicaijiaoyu.utils.VersionUtil;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View logoutBtn;

    public void onEventMainThread(UserLoginEvent e) {
        if (AccountUtil.isLoggedIn(getActivity()))
            logoutBtn.setVisibility(View.VISIBLE);
        else
            logoutBtn.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注册事件
        EventBus.getDefault().register(this);

        View view = inflater.inflate(R.layout.my_setting, container, false);
        //菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);
        //设置项
        ListView setting_list = (ListView) view.findViewById(R.id.setting_items);

        //意见反馈选项
        SettingEntity advises = new SettingEntity(getActivity().getString(R.string.advise), null, true);
        //清理缓存选项
        SettingEntity clean = new SettingEntity(getActivity().getString(R.string.clean), "10M", false);
        //当前版本选项
        SettingEntity version = new SettingEntity("当前版本", VersionUtil.getVersion(getActivity().getPackageManager(), getActivity().getPackageName()), false);

        List<SettingEntity> settings = new ArrayList<>();
        settings.add(advises);
        settings.add(clean);
        settings.add(version);

        setting_list.setAdapter(new SettingListAdapter(settings, getActivity()));

        setting_list.setOnItemClickListener(this);

        //退出登录键
        logoutBtn = view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(this);
        EventBus.getDefault().post(new UserLoginEvent());

        return view;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_menu_button:
                EventBus.getDefault().post(new MainMenuEvent());
                break;
            case R.id.logout_btn:
                SharedPreferenceUtil.getInstance(getActivity()).delete(new String[]{SharedPreferenceUtil.USER_ID});
                EventBus.getDefault().post(new UserLoginEvent());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //todo 打开意见反馈留言板
                break;
        }
    }
}
