package com.cjq.yicaijiaoyu.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjq.yicaijiaoyu.adapter.TeamListAdapter;
import com.cjq.yicaijiaoyu.entities.TeamDataEntity;
import com.markmao.pulltorefresh.widget.XListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CJQ on 2015/7/6.
 */
public class TeamIndexFragment extends Fragment{

    private XListView view;
    private List<TeamDataEntity> teamDataEntityList;
    private TeamListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = new XListView(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        view.setDivider(new ColorDrawable(Color.TRANSPARENT));

        //初始化就会请求列表
        requestTeamList();
        view.setAdapter(adapter);
        return view;
    }

    private void requestTeamList() {
        // TODO: 2015/7/7 获取到小组列表
        if(teamDataEntityList==null)
        teamDataEntityList=new ArrayList<>();
        teamDataEntityList.add(new TeamDataEntity("1","某个小组","某人","某个分类",new Date(),"某段描述",200,8,"https://www.baidu.com/img/bd_logo1.png"));
        teamDataEntityList.add(new TeamDataEntity("1","某个小组","某人","某个分类",new Date(),"某段描述",200,8,"https://www.baidu.com/img/bd_logo1.png"));
        teamDataEntityList.add(new TeamDataEntity("1","某个小组","某人","某个分类",new Date(),"某段描述",200,8,"https://www.baidu.com/img/bd_logo1.png"));
        teamDataEntityList.add(new TeamDataEntity("1","某个小组","某人","某个分类",new Date(),"某段描述",200,8,"https://www.baidu.com/img/bd_logo1.png"));
        teamDataEntityList.add(new TeamDataEntity("1","某个小组","某人","某个分类",new Date(),"某段描述",200,8,"https://www.baidu.com/img/bd_logo1.png"));
        teamDataEntityList.add(new TeamDataEntity("1","某个小组","某人","某个分类",new Date(),"某段描述",200,8,"https://www.baidu.com/img/bd_logo1.png"));
        if(adapter==null)
            adapter = new TeamListAdapter(null,getActivity());
        adapter.setTeamDataEntities(teamDataEntityList);
    }
}
