package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.LecInfoAdapter;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.entities.NeedVideoInfoEvent;
import com.cjq.yicaijiaoyu.entities.VideoInfoEvent;
import com.cjq.yicaijiaoyu.utils.ImageUtil;
import com.cjq.yicaijiaoyu.view.AllListView;
import com.ypy.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by CJQ on 2015/6/25.
 */
public class CourseInfoFragment extends Fragment {
    Course course;
    private View view;
    private AllListView lec_info;
    private TextView intro_text;

    public void onEventMainThread(VideoInfoEvent e) {
        course = e.getCourse();
        lec_info.setAdapter(new LecInfoAdapter(course,getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_info, container, false);

        lec_info = (AllListView) view.findViewById(R.id.lec_info);
        intro_text = (TextView) view.findViewById(R.id.intro_text);

        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new NeedVideoInfoEvent());
        return view;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}