package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.NeedVideoInfoEvent;
import com.cjq.yicaijiaoyu.entities.VideoInfoEvent;
import com.cjq.yicaijiaoyu.utils.ImageUtil;
import com.ypy.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by CJQ on 2015/6/25.
 */
public class CourseInfoFragment extends Fragment {

    private CircleImageView imageView;
    private TextView intro_text;
    private TextView lec_name;
    private TextView lec_tro;
    private View view;

    public void onEventMainThread(VideoInfoEvent e) {
        ImageUtil.LoadImage(getActivity(), e.getPortraitUrl(), imageView);
        lec_name.setText(e.getLecName());
        lec_tro.setText(e.getLecInfo());
        intro_text.setText(e.getVideoInfo());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_info, container, false);

        imageView = (CircleImageView) view.findViewById(R.id.menu_portrait);
        lec_name = (TextView) view.findViewById(R.id.lec_name);
        lec_tro = (TextView) view.findViewById(R.id.lec_tro);
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